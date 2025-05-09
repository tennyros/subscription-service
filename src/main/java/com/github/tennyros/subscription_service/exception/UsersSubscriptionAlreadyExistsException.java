package com.github.tennyros.subscription_service.exception;

public class UsersSubscriptionAlreadyExistsException extends RuntimeException {

    public UsersSubscriptionAlreadyExistsException(String message) {
        super(message);
    }

}
