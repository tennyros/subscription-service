package com.github.tennyros.subscription_service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request payload for user")
public record UserRequest(

        @Schema(description = "Valid email address", example = "user@example.com")
        @NotBlank @Email(message = "Email must be valid") @Size(max = 255, message = "Email is too long")
        String email

) {
}
