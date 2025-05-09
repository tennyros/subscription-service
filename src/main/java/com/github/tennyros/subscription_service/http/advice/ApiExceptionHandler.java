package com.github.tennyros.subscription_service.http.advice;

import com.github.tennyros.subscription_service.exception.InvalidServiceException;
import com.github.tennyros.subscription_service.exception.SubscriptionNotFoundException;
import com.github.tennyros.subscription_service.exception.UsersSubscriptionAlreadyExistsException;
import com.github.tennyros.subscription_service.exception.UserAlreadyExistsException;
import com.github.tennyros.subscription_service.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        log.error("User not found: {}", ex.getMessage());
        return buildProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "Not found", request);
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ProblemDetail handleSubscriptionNotFound(SubscriptionNotFoundException ex, HttpServletRequest request) {
        log.error("Subscription not found: {}", ex.getMessage());
        return buildProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "Not found", request);
    }

    @ExceptionHandler(InvalidServiceException.class)
    public ProblemDetail handleInvalidService(InvalidServiceException ex, HttpServletRequest request) {
        log.error("Such subscription service does not exist: {}", ex.getMessage());
        return buildProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), "Bad request", request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExists(UserAlreadyExistsException ex, HttpServletRequest request) {
        log.error("User with such email already exists: {}", ex.getMessage());
        return buildProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "Conflict", request);
    }

    @ExceptionHandler(UsersSubscriptionAlreadyExistsException.class)
    public ProblemDetail handleSubscriptionAlreadyExists(UsersSubscriptionAlreadyExistsException ex, HttpServletRequest request) {
        log.error("User already have such subscription: {}", ex.getMessage());
        return buildProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "Conflict", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.error("Validation failed for request {}: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = buildProblemDetail(HttpStatus.BAD_REQUEST, "One or more fields are invalid",
                "Validation failed", request);

        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    Map<String, Object> errorDetail = new HashMap<>();
                    errorDetail.put("field", error.getField());
                    errorDetail.put("message", error.getDefaultMessage());
                    errorDetail.put("rejectedValue", error.getRejectedValue());
                    return errorDetail;
                })
                .toList();

        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String message,
                                             String title, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setTitle(title);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", OffsetDateTime.now());
        return problemDetail;
    }

}
