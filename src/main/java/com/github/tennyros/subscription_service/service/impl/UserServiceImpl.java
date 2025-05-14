package com.github.tennyros.subscription_service.service.impl;

import com.github.tennyros.subscription_service.exception.UserAlreadyExistsException;
import com.github.tennyros.subscription_service.exception.UserNotFoundException;
import com.github.tennyros.subscription_service.entity.User;
import com.github.tennyros.subscription_service.repository.UserRepository;
import com.github.tennyros.subscription_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for user management operations.
 * <p>
 * Provides CRUD functionality for {@link User} entities including creation,
 * retrieval, update, and deletion. All methods are transactional by default.
 * </p>
 *
 * @see User
 * @see UserService
 * @see UserRepository
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Creates a new user after validating email uniqueness.
     * <p>
     * The operation will fail if a user with the same email already exists.
     * </p>
     *
     * @param user the user entity to create (must not be {@code null})
     * @return the persisted user entity with generated ID
     * @throws UserAlreadyExistsException if a user with the same email exists
     */
    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("User with email {} already exists", user.getEmail());
            throw new UserAlreadyExistsException("User with such email already exists");
        }
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by ID including their subscriptions.
     *
     * @param id the user ID to search for (must not be {@code null})
     * @return the found user entity with loaded subscriptions
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findWithSubscriptionsById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Updates an existing user's information.
     *
     * @param id   the ID of the user to update (must not be {@code null})
     * @param user the new user data containing updated fields (must not be {@code null})
     * @return the updated user entity
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findWithSubscriptionsById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user by ID after verifying existence.
     *
     * @param id the ID of the user to delete (must not be {@code null})
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            log.warn("User with ID {} does not exist", id);
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}
