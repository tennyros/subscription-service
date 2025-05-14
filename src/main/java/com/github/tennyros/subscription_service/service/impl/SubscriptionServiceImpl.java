package com.github.tennyros.subscription_service.service.impl;

import com.github.tennyros.subscription_service.dto.response.TopSubscriptions;
import com.github.tennyros.subscription_service.entity.Subscription;
import com.github.tennyros.subscription_service.entity.User;
import com.github.tennyros.subscription_service.exception.InvalidServiceException;
import com.github.tennyros.subscription_service.exception.SubscriptionNotFoundException;
import com.github.tennyros.subscription_service.exception.UserNotFoundException;
import com.github.tennyros.subscription_service.exception.UsersSubscriptionAlreadyExistsException;
import com.github.tennyros.subscription_service.repository.SubscriptionRepository;
import com.github.tennyros.subscription_service.repository.UserRepository;
import com.github.tennyros.subscription_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Service implementation for managing user subscriptions.
 * <p>
 * Provides business logic for:
 * <ul>
 *   <li>Adding new subscriptions with service validation</li>
 *   <li>Checking allowed services list</li>
 *   <li>Enforcing subscription uniqueness per user</li>
 * </ul>
 *
 * <p><b>Allowed services:</b> YouTube Premium, Netflix, Яндекс.Плюс, VK Музыка</p>
 *
 * @see Subscription
 * @see SubscriptionService
 * @see SubscriptionRepository
 */
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

    /**
     * Adds a subscription to the specified user after validating the service and user existence.
     *
     * @param userId       the ID of the user to whom the subscription will be added (must not be {@code null})
     * @param subscription the subscription to add. Must not be null, and its service must be in the allowed services list
     * @return the saved {@link Subscription} entity with generated ID and linked user
     * @throws InvalidServiceException                 if the subscription's service is not in the allowed services list
     * @throws UserNotFoundException                   if no user is found with the given ID
     * @throws UsersSubscriptionAlreadyExistsException if the user already has an active subscription
     *                                                 for the same service (case-insensitive check)
     */
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


    /**
     * Gets a list of subscriptions which belongs to existing user.
     *
     * @param userId the ID of the user to whom the subscription will be added (must not be {@code null})
     * @return the list of user's subscriptions
     * @throws UserNotFoundException if no user is found with the given ID
     */
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

    /**
     * Cancels a user's subscription after validating:
     * <ol>
     *   <li>User existence</li>
     *   <li>Subscription ownership</li>
     * </ol>
     *
     * <p>Physically deletes the subscription record from the database.</p>
     *
     * @param userId         the ID of the user who owns the subscription (must not be {@code null})
     * @param subscriptionId the ID of the subscription to cancel (must not be {@code null})
     * @throws UserNotFoundException         if no user exists with the specified ID
     * @throws SubscriptionNotFoundException if subscription doesn't exist or doesn't belong to the user
     */
    @Override
    public void cancelUserSubscription(Long userId, Long subscriptionId) {
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

    /**
     * Retrieves the top 3 most popular subscriptions across all users.
     * <p>
     * Popularity is determined by the number of active subscriptions for each service.
     * The result is ordered by subscription count in descending order.
     * </p>
     *
     * @return a list of {@link TopSubscriptions} DTOs containing:
     * <ul>
     *   <li>{@code serviceName} - the name of the service</li>
     *   <li>{@code subscriptionCount} - total active subscriptions count</li>
     * </ul>
     */
    @Override
    @Transactional(readOnly = true)
    public List<TopSubscriptions> getTop3Subscriptions() {
        return subscriptionRepository.findTop3PopularSubscriptions();
    }

}
