package com.github.tennyros.subscription_service.mapper;

import com.github.tennyros.subscription_service.dto.request.UserRequest;
import com.github.tennyros.subscription_service.dto.response.UserResponse;
import com.github.tennyros.subscription_service.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity_mapsUserRequestToUser() {
        UserRequest userRequest = new UserRequest("test@example.com");

        User user = userMapper.toEntity(userRequest);

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void toResponse_mapsUserToUserResponse() {
        User user = new User(1L, "test@example.com", new ArrayList<>());

        UserResponse userResponse = userMapper.toResponse(user);

        assertNotNull(userResponse);
        assertEquals(1L, userResponse.id());
        assertEquals("test@example.com", userResponse.email());
    }
}