package cz.cvut.moviemate.userservice.service;

import cz.cvut.moviemate.userservice.model.AppUser;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {

    String extractUsername(String token);

    String generateAccessToken(AppUser appUser);

    String generateRefreshToken(AppUser appUser);

    boolean validateToken(String token, UserDetails userDetails);
}
