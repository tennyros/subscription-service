package com.github.tennyros.subscription_service.service;

import com.github.tennyros.subscription_service.model.Subscription;
import java.util.List;

public interface SubService {

    Subscription addSubscription(Long userId, Subscription sub);

    List<Subscription> getUserSubscriptions(Long userId);

    void deleteSubscription(Long subId);

    List<Object[]> getTop3Subs();

}
