package com.github.tennyros.subscription_service.excepton;

public class SuchUsersSubscriptionAlreadyExists extends RuntimeException {

    public SuchUsersSubscriptionAlreadyExists(String message) {
        super(message);
    }

}
