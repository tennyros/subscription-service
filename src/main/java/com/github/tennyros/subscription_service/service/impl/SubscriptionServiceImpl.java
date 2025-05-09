package com.github.tennyros.subscription_service.service.impl;

import com.github.tennyros.subscription_service.dto.response.TopSubscriptions;
import com.github.tennyros.subscription_service.exception.InvalidServiceException;
import com.github.tennyros.subscription_service.exception.SubscriptionNotFoundException;
import com.github.tennyros.subscription_service.exception.UsersSubscriptionAlreadyExistsException;
import com.github.tennyros.subscription_service.exception.UserNotFoundException;
import com.github.tennyros.subscription_service.model.Subscription;
import com.github.tennyros.subscription_service.model.User;
import com.github.tennyros.subscription_service.repository.SubscriptionRepository;
import com.github.tennyros.subscription_service.repository.UserRepository;
import com.github.tennyros.subscription_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
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
        String serviceName = subscription.getServiceName();

        if (!allowedServices.contains(subscription.getServiceName())) {
            log.warn("Invalid subscription service: '{}'", serviceName);
            throw new InvalidServiceException("Such %s subscription service does not exist"
                    .formatted(subscription.getServiceName()));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found when adding subscription, ID: {}", userId);
                    return new UserNotFoundException(userId);
                });

        boolean alreadyExists = user.getSubscriptions().stream()
                .anyMatch(s -> s.getServiceName().equalsIgnoreCase(serviceName));

        if (alreadyExists) {
            log.warn("User with ID {} already has subscription: {}", userId, serviceName);
            throw new UsersSubscriptionAlreadyExistsException("User already has %s subscription"
                    .formatted(serviceName));
        }

        subscription.setUser(user);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        log.info("Added subscription: ID {}, user ID {}, service: '{}'",
                savedSubscription.getId(), userId, savedSubscription.getServiceName());
        return savedSubscription;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Subscription> getUserSubscriptions(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("User not found when fetching subscriptions, ID: {}", userId);
            throw new UserNotFoundException(userId);
        }

        List<Subscription> result = subscriptionRepository.findByUserId(userId);
        log.debug("Found {} subscriptions for user ID: {}", result.size(), userId);
        return result;
    }

    @Override
    public void deleteUserSubscription(Long userId, Long subscriptionId) {
        if (!userRepository.existsById(userId)) {
            log.warn("User not found when deleting subscription, ID: {}", userId);
            throw new UserNotFoundException(userId);
        }

        int deleted = subscriptionRepository.deleteByIdAndUserId(subscriptionId, userId);

        if (deleted == 0) {
            log.warn("Subscription not found when deleting: ID {} for user ID {}", subscriptionId, userId);
            throw new SubscriptionNotFoundException("Subscription with ID %d for user with ID %d not found"
                    .formatted(subscriptionId, userId));
        }

        log.info("Deleted subscription ID {} for user ID {}", subscriptionId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopSubscriptions> getTop3Subs() {
        return subscriptionRepository.findTop3PopularSubscriptions();
    }

}
