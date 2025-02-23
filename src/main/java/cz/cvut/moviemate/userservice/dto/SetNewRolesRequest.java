package cz.cvut.moviemate.userservice.dto;

import cz.cvut.moviemate.userservice.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public record SetNewRolesRequest(
        @Schema(description = "Roles to add", example = "[\"USER\", \"ADMIN\"]")
        Role[] roles
) implements Serializable {
}
