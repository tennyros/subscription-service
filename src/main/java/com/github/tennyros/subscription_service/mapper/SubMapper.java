package com.github.tennyros.subscription_service.mapper;

import com.github.tennyros.subscription_service.dto.request.SubRequest;
import com.github.tennyros.subscription_service.dto.response.SubResponse;
import com.github.tennyros.subscription_service.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubMapper {

    Subscription toEntity(SubRequest dto);

    SubResponse toResponse(Subscription sub);

}
