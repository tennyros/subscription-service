package com.github.tennyros.subscription_service.rest.controller;

import com.github.tennyros.subscription_service.dto.request.UserRequest;
import com.github.tennyros.subscription_service.dto.response.UserResponse;
import com.github.tennyros.subscription_service.mapper.UserMapper;
import com.github.tennyros.subscription_service.entity.User;
import com.github.tennyros.subscription_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(
        name = "Users",
        description = "Operations related to user management: creating, retrieving, updating, and deleting users"
)
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @Operation(
            summary = "New user creation",
            description = "Creates a new user account with the provided data. " +
                    "The email must be unique. Returns the created user along with its assigned ID.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Fields validation error",
                            content = @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "409", description = "User with such email already exists",
                            content = @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        log.info("Creating user with email: {}", request.email());
        User createdUser = userService.createUser(userMapper.toEntity(request));
        UserResponse response = userMapper.toResponse(createdUser);
        log.debug("Created user with ID: {}", response.id());
        URI location = URI.create("/users/" + response.id());
        return ResponseEntity
                .created(location)
                .body(response);
    }

    @Operation(
            summary = "Get user",
            description = "Get user by user ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        log.debug("Fetching user with ID: {}", id);
        UserResponse response = userMapper.toResponse(userService.getUserById(id));
        log.debug("Fetched user with ID: {}", id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "User update",
            description = "Update user by user ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated user",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserRequest request) {

        log.info("Updating user with ID: {}", id);
        User updatedUser = userService.updateUser(id, userMapper.toEntity(request));

        UserResponse response = userMapper.toResponse(updatedUser);
        log.debug("Updated user with ID: {}", id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(
            summary = "Delete user",
            description = "Delete user by user ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User has deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        log.debug("Deleted user with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

}
