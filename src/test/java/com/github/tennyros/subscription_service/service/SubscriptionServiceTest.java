package com.github.tennyros.subscription_service.service;

import com.github.tennyros.subscription_service.dto.response.TopSubscriptions;
import com.github.tennyros.subscription_service.exception.InvalidServiceException;
import com.github.tennyros.subscription_service.exception.SubscriptionNotFoundException;
import com.github.tennyros.subscription_service.exception.UsersSubscriptionAlreadyExistsException;
import com.github.tennyros.subscription_service.exception.UserNotFoundException;
import com.github.tennyros.subscription_service.entity.Subscription;
import com.github.tennyros.subscription_service.entity.User;
import com.github.tennyros.subscription_service.repository.SubscriptionRepository;
import com.github.tennyros.subscription_service.repository.UserRepository;
import com.github.tennyros.subscription_service.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    private static final long ID = 1L;
    private final Subscription sampleSubscription = Subscription.builder()
            .id(ID)
            .serviceName("Netflix")
            .build();
    private final User sampleUser = User.builder()
            .id(ID)
            .build();

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;


    @Test
    void addSubscription_returnsSubscription_whenServiceIsValid() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(sampleUser));
        when(subscriptionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Subscription result = subscriptionService.addSubscription(ID, sampleSubscription);

        assertEquals(sampleUser, result.getUser());
        verify(subscriptionRepository).save(sampleSubscription);
    }

    @Test
    void addSubscription_throwsException_whenServiceIsInvalid() {
        sampleSubscription.setServiceName("UnknownService");

        InvalidServiceException ex = assertThrows(InvalidServiceException.class,
                () -> subscriptionService.addSubscription(ID, sampleSubscription));

        assertEquals("Such UnknownService subscription service does not exist", ex.getMessage());
        verifyNoInteractions(userRepository);
        verifyNoInteractions(subscriptionRepository);
    }

    @Test
    void addSubscription_throwsException_whenUserNotFound() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> subscriptionService.addSubscription(ID, sampleSubscription));

        verify(userRepository).findById(ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void addSubscription_throwsSuchUsersSubscriptionAlreadyExists_whenSubscriptionAlreadyExists() {
        String serviceName = "Netflix";

        Subscription existingSub = new Subscription();
        existingSub.setServiceName(serviceName);

        Subscription newSub = new Subscription();
        newSub.setServiceName(serviceName);

        User user = new User();
        user.setId(ID);
        user.setSubscriptions(List.of(existingSub));

        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        assertThrows(UsersSubscriptionAlreadyExistsException.class, () -> {
            subscriptionService.addSubscription(ID, newSub);
        });

        verify(userRepository).findById(ID);
        verifyNoMoreInteractions(subscriptionRepository);
    }

    @Test
    void getUserSubscriptions_returnsSubscriptions_whenUserExists() {
        List<Subscription> list = List.of(sampleSubscription);

        when(userRepository.existsById(ID)).thenReturn(true);
        when(subscriptionRepository.findByUserId(ID)).thenReturn(list);

        List<Subscription> result = subscriptionService.getUserSubscriptions(ID);

        assertEquals(list, result);
        verify(subscriptionRepository).findByUserId(ID);
    }

    @Test
    void deleteSubscription_returnsNoContent_whenSuccessful() {
        when(userRepository.existsById(ID)).thenReturn(true);
        when(subscriptionRepository.deleteByIdAndUserId(ID, ID)).thenReturn(1);

        subscriptionService.cancelUserSubscription(ID, ID);

        verify(subscriptionRepository).deleteByIdAndUserId(ID, ID);
    }

    @Test
    void cancelUserSubscription_throwsException_whenNotFound() {
        when(userRepository.existsById(ID)).thenReturn(true);
        when(subscriptionRepository.deleteByIdAndUserId(ID, ID)).thenReturn(0);

        assertThrows(SubscriptionNotFoundException.class,
                () -> subscriptionService.cancelUserSubscription(ID, ID));

        verify(subscriptionRepository).deleteByIdAndUserId(ID, ID);
    }

    @Test
    void getTop3Subscriptions_returnsList() {
        List<TopSubscriptions> projections = List.of(
                new TopSubscriptions("Netflix", 100L),
                new TopSubscriptions("YouTube Premium", 50L)
        );

        when(subscriptionRepository.findTop3PopularSubscriptions()).thenReturn(projections);

        List<TopSubscriptions> result = subscriptionService.getTop3Subscriptions();

        assertEquals(2, result.size());
        assertEquals("Netflix", result.get(0).serviceName());
    }
}
