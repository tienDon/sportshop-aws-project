package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailOtpRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String otp;
}
