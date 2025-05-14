package com.github.tennyros.subscription_service.mapper;

import com.github.tennyros.subscription_service.dto.request.SubscriptionRequest;
import com.github.tennyros.subscription_service.dto.response.SubscriptionResponse;
import com.github.tennyros.subscription_service.entity.Subscription;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class SubscriptionMapperImplTest {

    private final SubscriptionMapper subscriptionMapper = Mappers.getMapper(SubscriptionMapper.class);

    @Test
    void shouldMapRequestToEntityCorrectly_whenValidRequest() {
        SubscriptionRequest request = new SubscriptionRequest(
                "Netflix",
                LocalDate.of(2024, 1, 1)
        );

        Subscription entity = subscriptionMapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getServiceName()).isEqualTo("Netflix");
        assertThat(entity.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(entity.getId()).isNull();
        assertThat(entity.getUser()).isNull();
    }

    @Test
    void shouldMapEntityToResponseCorrectly_whenValidEntity() {
        Subscription entity = Subscription.builder()
                .id(42L)
                .serviceName("YouTube Premium")
                .startDate(LocalDate.of(2023, 12, 1))
                .build();

        SubscriptionResponse response = subscriptionMapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(42L);
        assertThat(response.serviceName()).isEqualTo("YouTube Premium");
        assertThat(response.startDate()).isEqualTo(LocalDate.of(2023, 12, 1));
    }

    @Test
    void shouldReturnNull_whenRequestIsNull() {
        assertThat(subscriptionMapper.toEntity(null)).isNull();
    }

    @Test
    void shouldReturnNull_whenEntityIsNull() {
        assertThat(subscriptionMapper.toResponse(null)).isNull();
    }
}
