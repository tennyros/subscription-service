package com.github.tennyros.subscription_service.http.rest;

import com.github.tennyros.subscription_service.dto.response.TopSubscriptions;
import com.github.tennyros.subscription_service.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Subscriptions", description = "Operations related to subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "Get top 3 subscriptions",
            description = "Returns a list of the top 3 most popular subscriptions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful response",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = TopSubscriptions.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/top")
    public ResponseEntity<List<TopSubscriptions>> getTopSubscriptions() {
        log.info("Fetching top 3 popular subscriptions");
        return ResponseEntity.ok(subscriptionService.getTop3Subs());
    }
}
