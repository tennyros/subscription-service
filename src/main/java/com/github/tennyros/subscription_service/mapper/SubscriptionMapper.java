package com.github.tennyros.subscription_service.mapper;

import com.github.tennyros.subscription_service.dto.request.SubscriptionRequest;
import com.github.tennyros.subscription_service.dto.response.SubscriptionResponse;
import com.github.tennyros.subscription_service.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubscriptionMapper {

    Subscription toEntity(SubscriptionRequest dto);

    SubscriptionResponse toResponse(Subscription sub);

}
