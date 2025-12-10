package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateProfileRequest {

    private String fullName;      // tên hiển thị
    private String gender;
    private LocalDate dateOfBirth;
}
