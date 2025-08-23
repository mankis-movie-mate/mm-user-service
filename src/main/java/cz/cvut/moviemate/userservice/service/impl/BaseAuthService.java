package cz.cvut.moviemate.userservice.service.impl;

import cz.cvut.moviemate.userservice.dto.*;
import cz.cvut.moviemate.userservice.dto.mapper.AppUserMapper;
import cz.cvut.moviemate.userservice.exception.JwtErrorException;
import cz.cvut.moviemate.userservice.model.AppUser;
import cz.cvut.moviemate.userservice.model.Role;
import cz.cvut.moviemate.userservice.model.UserHistory;
import cz.cvut.moviemate.userservice.model.UserRole;
import cz.cvut.moviemate.userservice.model.event.UserLoginEvent;
import cz.cvut.moviemate.userservice.model.event.UserRegisterEvent;
import cz.cvut.moviemate.userservice.service.AuthService;
import cz.cvut.moviemate.userservice.service.InternalAppUserService;
import cz.cvut.moviemate.userservice.service.TokenService;
import cz.cvut.moviemate.userservice.util.ValidationUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j(topic = "BASE_AUTH_SERVICE")
@RequiredArgsConstructor
public class BaseAuthService implements AuthService {
    private final AppUserMapper appUserMapper;
    private final InternalAppUserService internalAppUserService;
    private final TokenService tokenService;
    private final ValidationUtil validationUtil;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final DaprEventPublisher daprEventPublisher;

    @Override
    @Transactional
    public AuthorizationResponse login(LoginRequest loginRequest) {
        log.debug("Login request: {}", loginRequest);

        AppUser appUser = loginRequest.identifier().contains("@")
                ? internalAppUserService.findUserByEmail(loginRequest.identifier())
                : internalAppUserService.findUserByUsername(loginRequest.identifier());
        validationUtil.checkUserBannedOrDeleted(appUser);

        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                appUser.getUsername(),
                loginRequest.password()
        ));

        // --- Publish event after successful login ---
        daprEventPublisher
                .publishLoginEvent(new UserLoginEvent(
                        String.valueOf(appUser.getId()),
                        appUser.getUsername(),
                        appUser.getEmail()
                ));

        return buildResponse(appUser);
    }

    @Override
    @Transactional
    public AuthorizationResponse register(RegisterRequest registerRequest) {
        log.debug("Register request: {}", registerRequest);
        validateRegisterRequest(registerRequest);

        String hashedPassword = passwordEncoder.encode(registerRequest.password());
        UserRole userRole = internalAppUserService.findUserRoleByRole(Role.USER);
        AppUser appUser = AppUser.builder()
                .username(registerRequest.username())
                .email(registerRequest.email())
                .fullName(registerRequest.fullName())
                .password(hashedPassword)
                .build();

        UserHistory userHistory = UserHistory
                .builder()
                .appUser(appUser)
                .createdAt(LocalDateTime.now())
                .build();

        appUser.setUserHistory(userHistory);
        appUser.addRole(userRole);
        AppUser saved = internalAppUserService.save(appUser);

        // --- Publish event after successful registration ---
        daprEventPublisher
                .publishRegisterEvent(new UserRegisterEvent(
                        String.valueOf(saved.getId()),
                        saved.getUsername(),
                        saved.getEmail()
                ));

        return buildResponse(saved);
    }

    @Override
    public TokenPairDto refreshToken(String refreshToken) {
        try {
            AppUser appUser = extractAppUser(refreshToken);

            if (tokenService.validateToken(refreshToken, appUser)) {
                String newToken = tokenService.generateAccessToken(appUser);
                return new TokenPairDto(newToken, refreshToken);
            } else throw new JwtErrorException("Refresh token is invalid.");
        } catch (JwtException e) {
            log.error("Refresh token validation error {}", e.getMessage());
            throw new JwtErrorException("Something goes wrong while validating refresh token.");
        }
    }

    private AppUser extractAppUser(String token) {
        String username = tokenService.extractUsername(token);
        return internalAppUserService.findUserByUsername(username);
    }

    private AuthorizationResponse buildResponse(AppUser appUser) {
        String accessToken = tokenService.generateAccessToken(appUser);
        String refreshToken = tokenService.generateRefreshToken(appUser);
        AppUserDto userDetails = appUserMapper.toDto(appUser);

        return new AuthorizationResponse(
                userDetails,
                accessToken,
                refreshToken
        );
    }

    private void validateRegisterRequest(RegisterRequest registerRequest) {
        validationUtil.checkDuplicate(registerRequest.username().toLowerCase(), internalAppUserService::findUserByUsername, "username");
        validationUtil.checkDuplicate(registerRequest.email(), internalAppUserService::findUserByEmail, "email");
    }


    @Override
    public AppUserClaimsDetails validateToken(String token) {
        try {
            String username = tokenService.extractUsername(token);
            AppUser appUser = internalAppUserService.findUserByUsername(username);
            List<String> roleNames = appUserMapper.rolesToString(appUser);
            return new AppUserClaimsDetails(
                    String.valueOf(appUser.getId()),
                    appUser.getUsername(),
                    appUser.getEmail(),
                    roleNames,
                    token
            );
        } catch (io.jsonwebtoken.JwtException ex) {
            log.error("Access token is invalid: {}", ex.getMessage());
            throw new JwtException("Access token is invalid");
        }
    }
}