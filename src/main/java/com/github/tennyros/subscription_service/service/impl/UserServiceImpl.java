package com.github.tennyros.subscription_service.service.impl;

import com.github.tennyros.subscription_service.exception.UserAlreadyExistsException;
import com.github.tennyros.subscription_service.exception.UserNotFoundException;
import com.github.tennyros.subscription_service.model.User;
import com.github.tennyros.subscription_service.repository.UserRepository;
import com.github.tennyros.subscription_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("User with email {} already exists", user.getEmail());
            throw new UserAlreadyExistsException("User with such email already exists");
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findWithSubscriptionsById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findWithSubscriptionsById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            log.warn("User with ID {} does not exist", id);
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}
