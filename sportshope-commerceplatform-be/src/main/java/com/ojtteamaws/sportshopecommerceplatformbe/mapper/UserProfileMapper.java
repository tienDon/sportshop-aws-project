package com.ojtteamaws.sportshopecommerceplatformbe.mapper;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserAddressResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserPhoneResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserProfileResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.UserAddress;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.UserPhone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
    public interface UserProfileMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "primaryPhone", source = "user.phone")
    @Mapping(
            target = "gender",
            expression = "java(user.getGender() != null ? user.getGender().name() : null)"
    )
    @Mapping(target = "dateOfBirth", source = "user.dateOfBirth")
    @Mapping(target = "phones", source = "phones")
    @Mapping(target = "addresses", source = "addresses")
    UserProfileResponse toUserProfile(User user,
                                      List<UserPhone> phones,
                                      List<UserAddress> addresses);

    @Mapping(target = "id", source = "phone.id")
    @Mapping(target = "phoneNumber", source = "phone.phoneNumber")
    @Mapping(target = "defaultPhone", source = "phone.defaultPhone")
    UserPhoneResponse toPhoneResponse(UserPhone phone);

    @Mapping(target = "id", source = "address.id")
    @Mapping(target = "addressDetail", source = "address.addressDetail")
    @Mapping(target = "defaultAddress", source = "address.defaultAddress")
    UserAddressResponse toAddressResponse(UserAddress address);
}
