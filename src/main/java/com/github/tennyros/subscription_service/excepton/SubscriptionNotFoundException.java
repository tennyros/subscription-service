package com.github.tennyros.subscription_service.excepton;

public class SubscriptionNotFoundException extends RuntimeException {

    public SubscriptionNotFoundException(String message) {
        super(message);
    }

}
