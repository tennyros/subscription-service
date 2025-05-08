package com.github.tennyros.subscription_service.service;

import com.github.tennyros.subscription_service.model.User;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

}
