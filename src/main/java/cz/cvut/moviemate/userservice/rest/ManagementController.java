package cz.cvut.moviemate.userservice.rest;

import cz.cvut.moviemate.userservice.dto.AppUserDto;
import cz.cvut.moviemate.userservice.dto.PageDto;
import cz.cvut.moviemate.userservice.dto.SetNewRolesRequest;
import cz.cvut.moviemate.userservice.service.ExternalAppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
@Slf4j(topic = "MANAGEMENT_CONTROLLER")
@RequiredArgsConstructor
@Tag(name = "ManagementController", description = "Controller for managing administrative tasks such as role assignment, ban user etc.")
public class ManagementController {
    private final ExternalAppUserService externalAppUserService;

    @Operation(summary = "Searching users using filters.",
            description = """
                    EQUAL(':'),
                    NEGATION('!'),
                    CONTAIN('*'),
                    GREATER_THAN('>'),
                    LESS_THAN('<'),
                    LIKE('~'),
                    details: true/false - provide users' details like activity history etc.
                    Filter example: ?page=1&size=5&q=username:manki%26updateDate>2024-04-01
                    WARNING: after filter q=, use "%26" instead of "&"
                    """
    )
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PageDto.class)))
    @ApiResponse(responseCode = "403", description = "Access Denied. You do not have permission to access this resource.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'ROOT')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageDto<AppUserDto>> searchUsers(
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "size", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "username", required = false) String sortBy,
            @RequestParam(value = "order", defaultValue = "ASC", required = false) Sort.Direction order,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "details", defaultValue = "false", required = false) boolean details
    ) {
        log.info("MANAGEMENT: Received request to search users by filters: ?page={}&size={}&sortBy={}&order={}&q={}&details={}.",
                pageNo, pageSize, sortBy, order, query, details);

        PageDto<AppUserDto> pageDto = externalAppUserService
                .searchUsers(pageNo, pageSize, sortBy, order, query, details);

        return ResponseEntity.ok(pageDto);
    }

    @Operation(summary = "Get user's profile information by username. Accessible by roles ADMIN, MODERATOR, ROOT.")
    @ApiResponse(responseCode = "200", description = "User's profile information was successfully retrieved.", content = @Content(schema = @Schema(implementation = AppUserDto.class)))
    @ApiResponse(responseCode = "404", description = "User with given username not found.")
    @ApiResponse(responseCode = "403", description = "Access Denied. You do not have permission to access this resource.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'ROOT')")
    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUserDto> getUserProfileByIdentifier(
            @PathVariable String username,
            @RequestParam(value = "details", defaultValue = "false", required = false) boolean details
    ) {
        log.info("MANAGEMENT: Received request to get profile of user {}", username);

        AppUserDto appUserDto = externalAppUserService.getUserByUsername(username, details);
        return ResponseEntity.ok(appUserDto);
    }

    @Operation(summary = "Set new roles to user. Accessible by roles ADMIN, ROOT.")
    @ApiResponse(responseCode = "200", description = "Roles were added successfully!")
    @ApiResponse(responseCode = "404", description = "User with given username not found.")
    @ApiResponse(responseCode = "403", description = "Access Denied. You do not have permission to access this resource.")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    @PatchMapping(value = "/role/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppUserDto> addRoleToUser(@PathVariable String username, @RequestBody SetNewRolesRequest request) {
        log.info("MANAGEMENT: Received request to set new roles to user {}", username);

        AppUserDto updated = externalAppUserService.setNewRoles(username, request);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Ban user by username. Accessible by roles ADMIN, MODERATOR, ROOT.")
    @ApiResponse(responseCode = "200", description = "User was successfully banned.")
    @ApiResponse(responseCode = "404", description = "User with given username not found.")
    @ApiResponse(responseCode = "403", description = "Access Denied. You do not have permission to access this resource.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'ROOT')")
    @PatchMapping("/ban/{username}")
    public ResponseEntity<String> banUserByUsername(@PathVariable String username) {
        log.info("MANAGEMENT: Received request to ban user {}", username);

        AppUserDto dto = externalAppUserService.banUserByUsername(username);
        return ResponseEntity.ok(String.format("User %s was successfully banned.", dto.getUsername()));
    }

    @Operation(summary = "Unban user by username. Accessible by roles ADMIN, MODERATOR, ROOT.")
    @ApiResponse(responseCode = "200", description = "User was successfully unbanned.")
    @ApiResponse(responseCode = "404", description = "User with given username not found.")
    @ApiResponse(responseCode = "403", description = "Access Denied. You do not have permission to access this resource.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'ROOT')")
    @PatchMapping("/unban/{username}")
    public ResponseEntity<String> unbanUserByUsername(@PathVariable String username) {
        log.info("MANAGEMENT: Received request to unban user {}", username);

        AppUserDto dto = externalAppUserService.unbanUserByUsername(username);
        return ResponseEntity.ok(String.format("User %s was successfully unbanned.", dto.getUsername()));
    }

    @Operation(summary = "Delete user by username. Accessible by roles ADMIN, ROOT.")
    @ApiResponse(responseCode = "200", description = "User was successfully deleted.")
    @ApiResponse(responseCode = "404", description = "User with given username not found.")
    @ApiResponse(responseCode = "403", description = "Access Denied. You do not have permission to access this resource.")
    @PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        log.info("MANAGEMENT: Received request to delete user {}", username);

        externalAppUserService.deleteUserByUsername(username);
        return ResponseEntity.ok(String.format("User %s was successfully deleted.", username));
    }
}
