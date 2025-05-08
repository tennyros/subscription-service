package com.github.tennyros.subscription_service.service;

import com.github.tennyros.subscription_service.exception.UserAlreadyExistsException;
import com.github.tennyros.subscription_service.exception.UserNotFoundException;
import com.github.tennyros.subscription_service.model.User;
import com.github.tennyros.subscription_service.repository.UserRepository;
import com.github.tennyros.subscription_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final long ID = 1L;
    private final User testUser = User.builder()
            .email("test@example.com")
            .build();
    private final User updatedUser = User.builder()
            .email("updated@example.com")
            .build();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_returnsSavedUser_whenValid() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser(testUser);

        assertThat(result).isEqualTo(testUser);
        verify(userRepository).save(testUser);
    }

    @Test
    void createUser_throwsException_whenUserAlreadyExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(testUser));

        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_returnsUser_whenUserExists() {
        when(userRepository.findWithSubscriptionsById(ID))
                .thenReturn(Optional.of(testUser));

        User result = userService.getUserById(ID);

        assertThat(result).isEqualTo(testUser);
    }

    @Test
    void getUserById_throwsException_whenUserNotFound() {
        when(userRepository.findWithSubscriptionsById(ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with ID 1 not found");
    }

    @Test
    void updateUser_returnsUpdatedUser_whenUserExists() {
        when(userRepository.findWithSubscriptionsById(ID)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(ID, updatedUser);

        assertThat(result.getEmail()).isEqualTo("updated@example.com");
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUser_throwsException_whenUserAlreadyExists() {
        when(userRepository.findWithSubscriptionsById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(ID, updatedUser))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with ID 1 not found");
    }

    @Test
    void deleteUser_callsRepository_whenUserExists() {
        when(userRepository.existsById(ID)).thenReturn(true);

        doNothing().when(userRepository).deleteById(ID);

        userService.deleteUser(ID);

        verify(userRepository).deleteById(ID);
    }
}
