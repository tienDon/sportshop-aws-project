package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UpdateProfileRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UserAddressRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UserPhoneRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserAddressResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserPhoneResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserProfileResponse;

import java.util.List;

public interface IUserProfileService {

    UserProfileResponse getProfile(Long userId);

    UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request);

    List<UserPhoneResponse> getPhones(Long userId);

    UserPhoneResponse addPhone(Long userId, UserPhoneRequest request);

    UserPhoneResponse updatePhone(Long userId, Long phoneId, UserPhoneRequest request);

    void deletePhone(Long userId, Long phoneId);

    List<UserAddressResponse> getAddresses(Long userId);

    UserAddressResponse addAddress(Long userId, UserAddressRequest request);

    UserAddressResponse updateAddress(Long userId, Long addressId, UserAddressRequest request);

    void deleteAddress(Long userId, Long addressId);
}
