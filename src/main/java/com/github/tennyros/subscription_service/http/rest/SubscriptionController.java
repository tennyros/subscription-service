package com.github.tennyros.subscription_service.http.rest;

import com.github.tennyros.subscription_service.dto.response.TopSubscriptions;
import com.github.tennyros.subscription_service.mapper.TopSubscriptionsMapper;
import com.github.tennyros.subscription_service.repository.projection.TopSubscriptionsProjection;
import com.github.tennyros.subscription_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final TopSubscriptionsMapper topSubscriptionsMapper;

    @GetMapping("/top")
    public ResponseEntity<List<TopSubscriptions>> getTopSubscriptions() {
        List<TopSubscriptionsProjection> projections = subscriptionService.getTop3Subs();
        List<TopSubscriptions> responses = projections.stream()
                .map(topSubscriptionsMapper::toTopSubscriptions)
                .toList();

        return ResponseEntity.ok(responses);
    }
}
