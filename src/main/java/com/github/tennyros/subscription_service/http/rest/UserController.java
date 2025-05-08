package com.github.tennyros.subscription_service.http.rest;

import com.github.tennyros.subscription_service.dto.request.UserRequest;
import com.github.tennyros.subscription_service.dto.response.UserResponse;
import com.github.tennyros.subscription_service.mapper.UserMapper;
import com.github.tennyros.subscription_service.model.User;
import com.github.tennyros.subscription_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        log.debug("Fetching user with ID: {}", id);
        UserResponse response = userMapper.toResponse(userService.getUserById(id));
        return ResponseEntity.ok(response);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        log.debug("Deleted user with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

}
