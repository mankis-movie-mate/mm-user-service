package cz.cvut.moviemate.userservice.dto;

import java.io.Serializable;

public record AuthorizationResponse(
        AppUserDto userDetails,
        String accessToken,
        String refreshToken
) implements Serializable {
}
