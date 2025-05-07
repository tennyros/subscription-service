package com.github.tennyros.subscription_service.excepton;

public class InvalidServiceException extends RuntimeException {

    public InvalidServiceException(String message) {
        super(message);
    }

}
