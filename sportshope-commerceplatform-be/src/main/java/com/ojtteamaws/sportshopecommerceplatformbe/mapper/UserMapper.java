package com.ojtteamaws.sportshopecommerceplatformbe.mapper;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.AuthResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "accessToken", ignore = true)        // token sẽ set sau trong AuthService

    @Mapping(target = "fullName", source = "fullName")

    // VERIFIED = emailVerified OR phoneVerified
    @Mapping(target = "verified",
            expression = "java(user.isEmailVerified() || user.isPhoneVerified())")

    // ➕ thêm 2 field rất quan trọng
    @Mapping(target = "role", source = "role")
    @Mapping(target = "userId", source = "id")

    AuthResponse toAuthResponse(User user);
}
