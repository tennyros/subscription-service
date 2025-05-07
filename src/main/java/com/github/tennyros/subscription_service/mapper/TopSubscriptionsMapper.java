package com.github.tennyros.subscription_service.mapper;

import com.github.tennyros.subscription_service.dto.response.TopSubscriptions;
import com.github.tennyros.subscription_service.repository.projection.TopSubscriptionsProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TopSubscriptionsMapper {

    @Mapping(target = "subscriptionCount", source = "count")
    TopSubscriptions toTopSubscriptions(TopSubscriptionsProjection projection);

}
