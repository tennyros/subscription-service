package com.github.tennyros.subscription_service.service.impl;

import com.github.tennyros.subscription_service.excepton.UserNotFoundException;
import com.github.tennyros.subscription_service.model.User;
import com.github.tennyros.subscription_service.model.Subscription;
import com.github.tennyros.subscription_service.repository.SubRepository;
import com.github.tennyros.subscription_service.repository.UserRepository;
import com.github.tennyros.subscription_service.repository.projection.TopSubsProjection;
import com.github.tennyros.subscription_service.service.SubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubServiceImpl implements SubService {

    private final SubRepository subRepository;
    private final UserRepository userRepository;

    @Override
    public Subscription addSubscription(Long userId, Subscription sub) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        sub.setUser(user);
        return subRepository.save(sub);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subscription> getUserSubscriptions(Long userId) {
        return subRepository.findByUserId(userId);
    }

    @Override
    public void deleteSubscription(Long subscriptionId) {
        subRepository.deleteById(subscriptionId);
    }

    @Override
    @Transactional
    public void deleteUserSubscription(Long userId, Long subscriptionId) {
        int deleted = subRepository.deleteByIdAndUserId(subscriptionId, userId);
        if (deleted == 0) {
            throw new SubscriptionNotFoundException(subscriptionId);
        }
    }

}
