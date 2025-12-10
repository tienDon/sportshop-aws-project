package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @Email(message = "Invalid email format")
    private String email;
    private String phone;
    private String username;
    private String password;
    private String fullName;
}
