package cz.cvut.moviemate.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record RegisterRequest(
        @Schema(description = "username", example = "manki_228")
        @NotBlank(message = "Name cannot be blank.")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
        @Pattern(regexp = "\\w+", message = "Username can only contain letters, numbers, and underscores.")
        String username,

        @Schema(description = "email", example = "manki@gmail.com")
        @Email(message = "Invalid email format.")
        @NotBlank(message = "Email cannot be blank.")
        String email,

        @Schema(description = "full name", example = "Ronald MacDonald")
        @Pattern(regexp = "^[\\p{L}\\s'\\-.]+$", message = "Full name can only contain letters, spaces, hyphens, periods, and apostrophes.")
        String fullName,

        @Schema(description = "password", example = "12345678")
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 50 characters.")
        String password
) implements Serializable {
}
