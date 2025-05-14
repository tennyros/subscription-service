package com.github.tennyros.subscription_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tennyros.subscription_service.dto.request.SubscriptionRequest;
import com.github.tennyros.subscription_service.dto.response.SubscriptionResponse;
import com.github.tennyros.subscription_service.rest.controller.UserSubscriptionController;
import com.github.tennyros.subscription_service.mapper.SubscriptionMapper;
import com.github.tennyros.subscription_service.entity.Subscription;
import com.github.tennyros.subscription_service.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserSubscriptionController.class)
class UserSubscriptionControllerTest {

    private static final long ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubscriptionService subscriptionService;

    @MockitoBean
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addSubscription_returnsCreatedSubscription_whenValidRequest() throws Exception {
        SubscriptionRequest request = new SubscriptionRequest("VK music", LocalDate.of(2025, 1, 1));
        Subscription entity = Subscription.builder()
                .id(10L)
                .serviceName("VK music")
                .startDate(LocalDate.of(2025, 1, 1))
                .build();
        SubscriptionResponse response = new SubscriptionResponse(10L, "VK music", LocalDate.of(2025, 1, 1));

        when(subscriptionMapper.toEntity(request)).thenReturn(entity);
        when(subscriptionService.addSubscription(ID, entity)).thenReturn(entity);
        when(subscriptionMapper.toResponse(entity)).thenReturn(response);

        mockMvc.perform(post("/api/v1/users/{userId}/subscriptions", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/1/subscriptions/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.serviceName").value("VK music"))
                .andExpect(jsonPath("$.startDate").value("2025-01-01"));
    }

    @Test
    void getUserSubscriptions_returnsSubscriptions_whenUserExists() throws Exception {
        Subscription sub = Subscription.builder()
                .id(20L)
                .serviceName("Netflix")
                .startDate(LocalDate.of(2024, 12, 1))
                .build();
        SubscriptionResponse response = new SubscriptionResponse(20L, "Netflix", LocalDate.of(2024, 12, 1));

        when(subscriptionService.getUserSubscriptions(ID)).thenReturn(List.of(sub));
        when(subscriptionMapper.toResponse(sub)).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/{userId}/subscriptions", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(20))
                .andExpect(jsonPath("$[0].serviceName").value("Netflix"))
                .andExpect(jsonPath("$[0].startDate").value("2024-12-01"));
    }

    @Test
    void deleteUserSubscription_returnsNoContent_whenSuccessful() throws Exception {
        Long subId = 30L;

        mockMvc.perform(delete("/api/v1/users/{userId}/subscriptions/{subId}", ID, subId))
                .andExpect(status().isNoContent());

        verify(subscriptionService).cancelUserSubscription(ID, subId);
    }
}
