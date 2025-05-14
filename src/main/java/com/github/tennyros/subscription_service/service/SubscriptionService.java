package com.github.tennyros.subscription_service.service;

import com.github.tennyros.subscription_service.dto.response.TopSubscriptions;
import com.github.tennyros.subscription_service.entity.Subscription;

import java.util.List;

public interface SubscriptionService {

    Subscription addSubscription(Long userId, Subscription sub);

    List<Subscription> getUserSubscriptions(Long userId);

    void cancelUserSubscription(Long userId, Long subscriptionId);

    List<TopSubscriptions> getTop3Subscriptions();

}
