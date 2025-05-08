package com.github.tennyros.subscription_service.excepton;

public class UserNotFoundException extends RuntimeException {

    public static final String MESSAGE = "User with ID %d not found";

    public UserNotFoundException(Long userId) {
        super(String.format(MESSAGE, userId));
    }

}
