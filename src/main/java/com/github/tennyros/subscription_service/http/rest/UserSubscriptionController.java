package com.github.tennyros.subscription_service.http.rest;

import com.github.tennyros.subscription_service.dto.request.SubscriptionRequest;
import com.github.tennyros.subscription_service.dto.response.SubscriptionResponse;
import com.github.tennyros.subscription_service.mapper.SubscriptionMapper;
import com.github.tennyros.subscription_service.model.Subscription;
import com.github.tennyros.subscription_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/subscriptions")
public class UserSubscriptionController {

    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> addSubscription(@PathVariable Long userId,
                                                                @RequestBody SubscriptionRequest request) {

        log.info("Adding subscription for user ID: {}", userId);
        Subscription subscription = subscriptionService.addSubscription(userId, subscriptionMapper.toEntity(request));
        SubscriptionResponse response = subscriptionMapper.toResponse(subscription);

        URI location = URI.create("/users/" + userId + "/subscriptions/" + subscription.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getUserSubscriptions(@PathVariable Long userId) {
        log.debug("Fetching subscriptions for user ID: {}", userId);
        List<SubscriptionResponse> responses = subscriptionService.getUserSubscriptions(userId).stream()
                .map(subscriptionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{subId}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable Long userId,
            @PathVariable Long subId) {

        log.info("Deleting subscription ID: {} for user ID: {}", subId, userId);
        subscriptionService.deleteUserSubscription(userId, subId);
        return ResponseEntity.noContent().build();
    }
}
