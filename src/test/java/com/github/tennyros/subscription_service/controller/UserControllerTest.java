package com.github.tennyros.subscription_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tennyros.subscription_service.dto.request.UserRequest;
import com.github.tennyros.subscription_service.dto.response.UserResponse;
import com.github.tennyros.subscription_service.rest.controller.UserController;
import com.github.tennyros.subscription_service.mapper.UserMapper;
import com.github.tennyros.subscription_service.entity.User;
import com.github.tennyros.subscription_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final long ID = 1L;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String UPDATED_EMAIL = "test@example.com";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_returns201Created_whenValidRequest() throws Exception {

        UserRequest userRequest = new UserRequest(TEST_EMAIL);
        User userEntity = new User(ID, TEST_EMAIL, new ArrayList<>());
        UserResponse userResponse = new UserResponse(ID, TEST_EMAIL, new ArrayList<>());

        when(userMapper.toEntity(userRequest)).thenReturn(userEntity);
        when(userService.createUser(userEntity)).thenReturn(userEntity);
        when(userMapper.toResponse(userEntity)).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/1"))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL));

        verify(userService).createUser(userEntity);
        verify(userMapper).toEntity(userRequest);
        verify(userMapper).toResponse(userEntity);
    }

    @Test
    void getUser_returnsUserResponse_whenUserExists() throws Exception {
        User user = new User(ID, TEST_EMAIL, new ArrayList<>());
        UserResponse userResponse = new UserResponse(ID, TEST_EMAIL, new ArrayList<>());

        when(userService.getUserById(ID)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/users/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL));
    }

    @Test
    void updateUser_returnsUpdatedUserResponse_whenUserExists() throws Exception {
        UserRequest userRequest = new UserRequest(UPDATED_EMAIL);
        User updatedUser = new User(ID, UPDATED_EMAIL, new ArrayList<>());
        UserResponse userResponse = new UserResponse(ID, UPDATED_EMAIL, new ArrayList<>());

        when(userMapper.toEntity(userRequest)).thenReturn(updatedUser);
        when(userService.updateUser(eq(ID), any(User.class))).thenReturn(updatedUser);
        when(userMapper.toResponse(updatedUser)).thenReturn(userResponse);

        mockMvc.perform(put("/api/v1/users/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.email").value(UPDATED_EMAIL));
    }

    @Test
    void deleteUser_returnsNoContent_whenUserExists() throws Exception {
        doNothing().when(userService).deleteUser(ID);

        mockMvc.perform(delete("/api/v1/users/{id}", ID))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(ID);
    }

}
