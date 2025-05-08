package com.github.tennyros.subscription_service.service.impl;

import com.github.tennyros.subscription_service.dto.response.TopSubscriptions;
import com.github.tennyros.subscription_service.excepton.InvalidServiceException;
import com.github.tennyros.subscription_service.excepton.SubscriptionNotFoundException;
import com.github.tennyros.subscription_service.excepton.SuchUsersSubscriptionAlreadyExists;
import com.github.tennyros.subscription_service.excepton.UserNotFoundException;
import com.github.tennyros.subscription_service.model.Subscription;
import com.github.tennyros.subscription_service.model.User;
import com.github.tennyros.subscription_service.repository.SubscriptionRepository;
import com.github.tennyros.subscription_service.repository.UserRepository;
import com.github.tennyros.subscription_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final Set<String> allowedServices = Set.of(
            "YouTube Premium", "Netflix", "Яндекс.Плюс", "VK Музыка"
    );

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    public Subscription addSubscription(Long userId, Subscription subscription) {

        if (!allowedServices.contains(subscription.getServiceName())) {
            throw new InvalidServiceException("Such %s subscription service does not exist"
                    .formatted(subscription.getServiceName()));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        boolean alreadyExists = user.getSubscriptions().stream()
                .anyMatch(s -> s.getServiceName().equalsIgnoreCase(subscription.getServiceName()));

        if (alreadyExists) {
            throw new SuchUsersSubscriptionAlreadyExists("User already has %s subscription"
                    .formatted(subscription.getServiceName()));
        }

        subscription.setUser(user);
        return subscriptionRepository.save(subscription);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Subscription> getUserSubscriptions(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public void deleteUserSubscription(Long userId, Long subscriptionId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        int deleted = subscriptionRepository.deleteByIdAndUserId(subscriptionId, userId);

        if (deleted == 0) {
            throw new SubscriptionNotFoundException("Subscription with ID %d for user with ID %d not found"
                    .formatted(subscriptionId, userId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopSubscriptions> getTop3Subs() {
        return subscriptionRepository.findTop3PopularSubscriptions();
    }

}
