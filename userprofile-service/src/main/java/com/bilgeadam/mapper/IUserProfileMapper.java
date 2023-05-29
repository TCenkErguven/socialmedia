package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.rabbitmq.model.UserProfileModel;
import com.bilgeadam.repository.entity.Follow;
import com.bilgeadam.repository.entity.UserProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserProfileMapper {

    IUserProfileMapper INSTANCE = Mappers.getMapper(IUserProfileMapper.class);

    UserProfile fromDtoToUserProfile(final NewCreateUserRequestDto dto);

    UserProfile fromRegisterModelToUserProfile(final RegisterModel registerModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserProfile updateFromDtoToUserProfile(UserProfileUpdateRequestDto dto, @MappingTarget UserProfile userProfile);

    UpdateEmailOrUsernameRequestDto toUpdateUsernameEmail(final UserProfile userProfile);

    UpdateEmailOrUsernameRequestDto toUpdateUsernameEmail(final UserProfileUpdateRequestDto dto);
    RegisterElasticModel fromUserProfileToRegisterElasticModel(final UserProfile userProfile);
    Follow fromCreateFollowDtoToFollow(final String followId, final String userId);
    ToAuthPasswordChangeDto fromUserProfileToAuthPasswordChangeDto(final UserProfile userProfile);

    UserProfileModel fromUserProfileToUserProfileModel(final UserProfile userProfile);
}
