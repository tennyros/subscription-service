package com.github.tennyros.subscription_service.exception;

public class SuchUsersSubscriptionAlreadyExists extends RuntimeException {

    public SuchUsersSubscriptionAlreadyExists(String message) {
        super(message);
    }

}
