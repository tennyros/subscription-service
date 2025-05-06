package com.github.tennyros.subscription_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "User profile information response")
public record UserResponse(

        @Schema(description = "Unique user identifier", example = "456", requiredMode = REQUIRED)
        Long id,

        @Schema(description = "Email address", example = "user@example.com", requiredMode = REQUIRED)
        String email,

        @Schema(description = "Users subscription list")
        List<SubResponse> subscriptions

) {
}
