package cz.cvut.moviemate.userservice.service.impl;

import cz.cvut.moviemate.userservice.exception.JwtErrorException;
import cz.cvut.moviemate.userservice.model.AppUser;
import cz.cvut.moviemate.userservice.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j(topic = "BASE_TOKEN_SERVICE")
public class BaseTokenService implements TokenService {
    @Value("${security.jwt.secret-key}")
    private String JWT_SECRET;

    @Value("${security.jwt.access-expiration}")
    private Long JWT_ACCESS_EXPIRATION_SECONDS;

    @Value("${security.jwt.refresh-expiration}")
    private Long JWT_REFRESH_EXPIRATION_SECONDS;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateAccessToken(AppUser appUser) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", appUser.getId());
            claims.put("username", appUser.getUsername());
            claims.put("email", appUser.getEmail());
            claims.put("roles", appUser.getAuthorities());
            return generateAccessToken(claims, appUser, JWT_ACCESS_EXPIRATION_SECONDS);
        } catch (JwtException e) {
            throw new JwtErrorException("Error while generating token: " + e.getMessage());
        }
    }

    @Override
    public String generateRefreshToken(AppUser appUser) {
        try {
            return generateAccessToken(new HashMap<>(), appUser, JWT_REFRESH_EXPIRATION_SECONDS);
        } catch (JwtException e) {
            throw new JwtErrorException("Error while generating token: " + e.getMessage());
        }
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails, Long expiration) {
        final Date currentDate = new Date(System.currentTimeMillis());
        final Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(currentDate)
                .expiration(expirationDate)
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getSecretKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}