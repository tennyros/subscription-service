package com.github.tennyros.subscription_service.service;

import com.github.tennyros.subscription_service.model.Subscription;
import com.github.tennyros.subscription_service.repository.projection.TopSubsProjection;

import java.util.List;

public interface SubService {

    Subscription addSubscription(Long userId, Subscription sub);

    List<Subscription> getUserSubscriptions(Long userId);

    void deleteUserSubscription(Long userId, Long subscriptionId);

    List<TopSubsProjection> getTop3Subs();

}
