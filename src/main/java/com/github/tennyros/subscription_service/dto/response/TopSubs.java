package com.github.tennyros.subscription_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Top subscriptions response")
public record TopSubs(

        @Schema(description = "Service name", example = "VK music")
        String serviceName,

        @Schema(description = "Subscriptions count", example = "3")
        Long subscriptionCount

) {

}
