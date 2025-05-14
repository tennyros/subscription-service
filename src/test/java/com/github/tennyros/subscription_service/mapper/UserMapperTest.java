package com.github.tennyros.subscription_service.mapper;

import com.github.tennyros.subscription_service.dto.request.UserRequest;
import com.github.tennyros.subscription_service.dto.response.SubscriptionResponse;
import com.github.tennyros.subscription_service.dto.response.UserResponse;
import com.github.tennyros.subscription_service.entity.Subscription;
import com.github.tennyros.subscription_service.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapperImpl userMapper = new UserMapperImpl();

    @Test
    void toEntity_mapsUserRequestToUser_whenValidRequest() {
        UserRequest request = new UserRequest("user@example.com");

        User user = userMapper.toEntity(request);

        assertNotNull(user);
        assertEquals("user@example.com", user.getEmail());
    }

    @Test
    void toEntity_returnsNull_whenRequestIsNull() {
        assertNull(userMapper.toEntity(null));
    }

    @Test
    void toResponse_mapsUserToUserResponse_whenValidUser() {
        List<Subscription> subs = List.of(
                new Subscription(1L, "Netflix", LocalDate.of(2024, 1, 1), null),
                new Subscription(2L, "Spotify", LocalDate.of(2024, 2, 1), null)
        );
        User user = User.builder()
                .id(10L)
                .email("test@example.com")
                .subscriptions(subs)
                .build();

        UserResponse response = userMapper.toResponse(user);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("test@example.com", response.email());
        assertEquals(2, response.subscriptions().size());

        SubscriptionResponse sub1 = response.subscriptions().get(0);
        assertEquals("Netflix", sub1.serviceName());
    }

    @Test
    void toResponse_returnsNull_whenUserIsNull() {
        assertNull(userMapper.toResponse(null));
    }

    @Test
    void subscriptionToSubscriptionResponse_returnsMappedObject() {
        Subscription sub = new Subscription(42L, "YouTube", LocalDate.of(2025, 5, 1), null);

        SubscriptionResponse response = userMapper.subscriptionToSubscriptionResponse(sub);

        assertNotNull(response);
        assertEquals(42L, response.id());
        assertEquals("YouTube", response.serviceName());
        assertEquals(LocalDate.of(2025, 5, 1), response.startDate());
    }

    @Test
    void subscriptionToSubscriptionResponse_returnsNull_whenNullPassed() {
        assertNull(userMapper.subscriptionToSubscriptionResponse(null));
    }

    @Test
    void subscriptionListToSubscriptionResponseList_mapsListCorrectly() {
        List<Subscription> list = List.of(
                new Subscription(1L, "VK", LocalDate.of(2025, 1, 1), null),
                new Subscription(2L, "Telegram", LocalDate.of(2025, 2, 1), null)
        );

        List<SubscriptionResponse> responseList = userMapper.subscriptionListToSubscriptionResponseList(list);

        assertNotNull(responseList);
        assertEquals(2, responseList.size());
        assertEquals("VK", responseList.get(0).serviceName());
    }

    @Test
    void subscriptionListToSubscriptionResponseList_returnsNull_whenListIsNull() {
        assertNull(userMapper.subscriptionListToSubscriptionResponseList(null));
    }
}