package cz.cvut.moviemate.userservice.rest;

import cz.cvut.moviemate.userservice.dto.AppUserDto;
import cz.cvut.moviemate.userservice.dto.UpdateUserRequest;
import cz.cvut.moviemate.userservice.dto.error.ApiErrorSingleResponse;
import cz.cvut.moviemate.userservice.service.ExternalAppUserService;
import cz.cvut.moviemate.userservice.util.PrincipalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Slf4j(topic = "APP_USER_CONTROLLER")
@RequiredArgsConstructor
@Tag(name = "AppUserController", description = "Controller for managing user data.")
public class AppUserController {
    private final ExternalAppUserService externalAppUserService;
    private final PrincipalUtil principalUtil;

    @Operation(summary = "Get user's profile")
    @ApiResponse(responseCode = "200", description = "User's profile information was successfully retrieved.")
    @ApiResponse(responseCode = "500", description = "Unable to fetch the username from the authentication context.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUserDto> getUserProfile() {
        log.info("Received request to get user's profile");
        String username = principalUtil.getPrincipalUsername();
        return ResponseEntity.ok(externalAppUserService.getUserByUsername(username, false));
    }

    @Operation(summary = "Update profile information")
    @ApiResponse(responseCode = "200", description = "User's profile information was successfully updated.")
    @ApiResponse(responseCode = "404", description = "User with given username not found.")
    @PatchMapping(
            value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUserDto> updateProfileInfo(@RequestBody @Valid UpdateUserRequest request) {
        log.info("Received request to update user info.");
        String username = principalUtil.getPrincipalUsername();
        return ResponseEntity.ok(externalAppUserService.updateUser(username, request));
    }
}
