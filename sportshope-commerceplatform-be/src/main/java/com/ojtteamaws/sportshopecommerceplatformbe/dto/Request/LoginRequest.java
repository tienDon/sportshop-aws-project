package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class LoginRequest {

    private String email;
    private String username;
    private String password;
    private String phone;
}
