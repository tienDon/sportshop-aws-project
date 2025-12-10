package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class SendEmailOtpRequest {
    @Email
    @NotBlank
    private String email;
}
