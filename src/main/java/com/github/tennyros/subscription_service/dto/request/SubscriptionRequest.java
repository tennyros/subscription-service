package com.github.tennyros.subscription_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Request payload for subscription")
public record SubscriptionRequest(

        @Schema(description = "Name of the subscription", example = "VK music", minLength = 2, maxLength = 64)
        @NotBlank(message = "Service name must be defined") @Size(min = 2, max = 64, message = "Service name is invalid")
        String serviceName,

        @Schema(description = "Subscription start date", example = "2025-01-01")
        @NotNull(message = "Subscription start date is required")
        @PastOrPresent(message = "Subscription start date must be today or earlie")
        LocalDate startDate

) {
}
