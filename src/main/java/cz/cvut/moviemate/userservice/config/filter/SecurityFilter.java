package cz.cvut.moviemate.userservice.config.filter;

import cz.cvut.moviemate.userservice.service.InternalAppUserService;
import cz.cvut.moviemate.userservice.service.TokenService;
import cz.cvut.moviemate.userservice.util.ValidationUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j(topic = "USER_SERVICE_SECURITY_FILTER")
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";

    private final TokenService tokenService;
    private final InternalAppUserService internalAppUserService;
    private final ValidationUtil validationUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER);

        if (!isBearerHeaderPresent(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        processToken(token, request);
        filterChain.doFilter(request, response);
    }

    private void processToken(String token, HttpServletRequest request) {
        String username = tokenService.extractUsername(token);

        if (isNotAuthenticated(username)) {
            UserDetails userDetails = internalAppUserService.loadUserByUsername(username);
            validationUtil.checkUserBannedOrDeleted(userDetails);

            if (tokenService.validateToken(token, userDetails))
                setAuthContext(userDetails, request);
        }
    }

    private void setAuthContext(UserDetails userDetails, HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getUsername(),
                userDetails.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }

    private boolean isNotAuthenticated(String username) {
        return StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private boolean isBearerHeaderPresent(String authHeader) {
        return !StringUtils.isEmpty(authHeader) || StringUtils.startsWith(authHeader, BEARER_PREFIX);
    }
}
