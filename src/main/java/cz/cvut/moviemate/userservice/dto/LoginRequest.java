package cz.cvut.moviemate.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record LoginRequest(
        @Schema(description = "Sign in with username/email", example = "manki/manki@gmail.com")
        @NotBlank(message = "Identifier cannot be blank.")
        @Size(min = 3, max = 50, message = "Identifier must be between 3 and 50 characters.")
        String identifier,

        @Schema(description = "password", example = "12345678")
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters.")
        String password
) implements Serializable {
}
