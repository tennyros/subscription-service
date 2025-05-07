package com.github.tennyros.subscription_service.service;

import com.github.tennyros.subscription_service.model.Subscription;
import com.github.tennyros.subscription_service.repository.projection.TopSubscriptionsProjection;

import java.util.List;

public interface SubscriptionService {

    Subscription addSubscription(Long userId, Subscription sub);

    List<Subscription> getUserSubscriptions(Long userId);

    void deleteUserSubscription(Long userId, Long subscriptionId);

    List<TopSubscriptionsProjection> getTop3Subs();

}
