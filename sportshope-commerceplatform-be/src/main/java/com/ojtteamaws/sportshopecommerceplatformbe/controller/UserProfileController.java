package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UpdateProfileRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UserAddressRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UserPhoneRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserAddressResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserPhoneResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserProfileResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final IUserProfileService userProfileService;

    private Long getUserId(Principal principal) {
        // JwtAuthFilter đang set principal = userId.toString()
        return Long.parseLong(principal.getName());
    }

    // ----- PROFILE -----
    @GetMapping("/profile")
    public UserProfileResponse getProfile(Principal principal) {
        Long userId = getUserId(principal);
        return userProfileService.getProfile(userId);
    }

    @PutMapping("/profile")
    public UserProfileResponse updateProfile(@RequestBody UpdateProfileRequest request,
                                             Principal principal) {
        Long userId = getUserId(principal);
        return userProfileService.updateProfile(userId, request);
    }

    // ----- PHONES -----
    @GetMapping("/phones")
    public List<UserPhoneResponse> getPhones(Principal principal) {
        Long userId = getUserId(principal);
        return userProfileService.getPhones(userId);
    }

    @PostMapping("/phones")
    public UserPhoneResponse addPhone(@RequestBody UserPhoneRequest request,
                                      Principal principal) {
        Long userId = getUserId(principal);
        return userProfileService.addPhone(userId, request);
    }

    @PutMapping("/phones/{id}")
    public UserPhoneResponse updatePhone(@PathVariable Long id,
                                         @RequestBody UserPhoneRequest request,
                                         Principal principal) {
        Long userId = getUserId(principal);
        return userProfileService.updatePhone(userId, id, request);
    }

    @DeleteMapping("/phones/{id}")
    public void deletePhone(@PathVariable Long id, Principal principal) {
        Long userId = getUserId(principal);
        userProfileService.deletePhone(userId, id);
    }

    // ----- ADDRESSES -----
    @GetMapping("/addresses")
    public List<UserAddressResponse> getAddresses(Principal principal) {
        Long userId = getUserId(principal);
        return userProfileService.getAddresses(userId);
    }

    @PostMapping("/addresses")
    public UserAddressResponse addAddress(@RequestBody UserAddressRequest request,
                                          Principal principal) {
        Long userId = getUserId(principal);
        return userProfileService.addAddress(userId, request);
    }

    @PutMapping("/addresses/{id}")
    public UserAddressResponse updateAddress(@PathVariable Long id,
                                             @RequestBody UserAddressRequest request,
                                             Principal principal) {
        Long userId = getUserId(principal);
        return userProfileService.updateAddress(userId, id, request);
    }

    @DeleteMapping("/addresses/{id}")
    public void deleteAddress(@PathVariable Long id, Principal principal) {
        Long userId = getUserId(principal);
        userProfileService.deleteAddress(userId, id);
    }
}
