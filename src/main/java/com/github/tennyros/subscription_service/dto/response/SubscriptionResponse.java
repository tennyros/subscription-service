package com.github.tennyros.subscription_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Subscription information response")
public record SubscriptionResponse(

        @Schema(description = "Unique subscription identifier", example = "123", requiredMode = REQUIRED)
        Long id,

        @Schema(description = "Subscription name", example = "VK music", requiredMode = REQUIRED)
        String serviceName,

        @Schema(description = "Subscription start date", example = "2025-01-01")
        LocalDate startDate

) {
}
