package com.github.tennyros.subscription_service.controller;

import com.github.tennyros.subscription_service.dto.response.TopSubscriptions;
import com.github.tennyros.subscription_service.http.rest.SubscriptionController;
import com.github.tennyros.subscription_service.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @Test
    void getTopSubscriptions_shouldReturnListOfTopSubscriptions() {
        List<TopSubscriptions> topSubs = List.of(
                new TopSubscriptions("YouTube", 15L),
                new TopSubscriptions("Spotify", 10L),
                new TopSubscriptions("Netflix", 7L)
        );
        when(subscriptionService.getTop3Subs()).thenReturn(topSubs);

        ResponseEntity<List<TopSubscriptions>> response = subscriptionController.getTopSubscriptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals("YouTube", response.getBody().get(0).serviceName());
        verify(subscriptionService, times(1)).getTop3Subs();
    }
}
