package com.github.tennyros.subscription_service.service.impl;

import com.github.tennyros.subscription_service.excepton.UserAlreadyExistsException;
import com.github.tennyros.subscription_service.excepton.UserNotFoundException;
import com.github.tennyros.subscription_service.model.User;
import com.github.tennyros.subscription_service.repository.UserRepository;
import com.github.tennyros.subscription_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with such email already exists");
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findWithSubscriptionsById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
