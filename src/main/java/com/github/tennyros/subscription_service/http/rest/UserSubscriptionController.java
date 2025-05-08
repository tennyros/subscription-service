package com.github.tennyros.subscription_service.http.rest;

import com.github.tennyros.subscription_service.dto.request.SubscriptionRequest;
import com.github.tennyros.subscription_service.dto.response.SubscriptionResponse;
import com.github.tennyros.subscription_service.dto.response.UserResponse;
import com.github.tennyros.subscription_service.mapper.SubscriptionMapper;
import com.github.tennyros.subscription_service.model.Subscription;
import com.github.tennyros.subscription_service.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
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
@Tag(
        name = "User Subscriptions",
        description = "Operations related to managing user subscriptions " +
                "such as adding, retrieving, and deleting subscriptions"
)
public class UserSubscriptionController {

    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "Add subscription to user",
            description = "Available services: YouTube Premium, Netflix, Яндекс.Плюс, VK Музыка",
            responses = {
                @ApiResponse(responseCode = "201", description = "Subscription added",
                        content = @Content(schema = @Schema(implementation = SubscriptionResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid service or validation error",
                        content = @Content(mediaType = "application/problem+json",
                                schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(responseCode = "404", description = "User not found",
                        content = @Content(mediaType = "application/problem+json",
                                schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PostMapping
    public ResponseEntity<SubscriptionResponse> addSubscription(@PathVariable Long userId,
                                                                @RequestBody SubscriptionRequest request) {

        log.info("Adding subscription for user ID: {}", userId);
        Subscription subscription = subscriptionService.addSubscription(userId, subscriptionMapper.toEntity(request));
        SubscriptionResponse response = subscriptionMapper.toResponse(subscription);

        URI location = URI.create("/users/" + userId + "/subscriptions/" + subscription.getId());
        return ResponseEntity.created(location).body(response);
    }

    @Operation(
            summary = "Get user subscriptions",
            description = "Get all subscriptions for user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of subscriptions",
                            content = @Content(schema = @Schema(implementation = SubscriptionResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getUserSubscriptions(@PathVariable Long userId) {
        log.debug("Fetching subscriptions for user ID: {}", userId);
        List<SubscriptionResponse> responses = subscriptionService.getUserSubscriptions(userId).stream()
                .map(subscriptionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Delete a user's subscription",
            description = "Delete a user's subscription by user ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Subscription has deleted"),
                    @ApiResponse(responseCode = "404", description = "User or subscription is not found",
                            content = @Content(mediaType = "application/problem+json",
                                    schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @DeleteMapping("/{subId}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable Long userId,
            @PathVariable Long subId) {

        log.info("Deleting subscription ID: {} for user ID: {}", subId, userId);
        subscriptionService.deleteUserSubscription(userId, subId);
        return ResponseEntity.noContent().build();
    }
}
