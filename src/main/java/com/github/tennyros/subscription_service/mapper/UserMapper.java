package com.github.tennyros.subscription_service.mapper;

import com.github.tennyros.subscription_service.dto.request.UserRequest;
import com.github.tennyros.subscription_service.dto.response.UserResponse;
import com.github.tennyros.subscription_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserRequest dto);

    UserResponse toResponse(User user);

}
