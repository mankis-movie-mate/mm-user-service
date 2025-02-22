package cz.cvut.moviemate.userservice.service;

import cz.cvut.moviemate.userservice.dto.*;

public interface AuthService {

    AuthorizationResponse login(LoginRequest loginRequest);

    AuthorizationResponse register(RegisterRequest registerRequest);

    TokenPairDto refreshToken(String refreshToken);

    AppUserClaimsDetails validateToken(String token);
}
