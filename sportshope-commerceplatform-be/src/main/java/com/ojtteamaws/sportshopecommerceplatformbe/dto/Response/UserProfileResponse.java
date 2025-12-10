package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserProfileResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String primaryPhone;      // số phone chính trong bảng users

    // "MALE"/"FEMALE"/"OTHER"
    private String gender;
    private LocalDate dateOfBirth;

    private List<UserPhoneResponse> phones;      // danh sách số phụ
    private List<UserAddressResponse> addresses; // danh sách địa chỉ
}
