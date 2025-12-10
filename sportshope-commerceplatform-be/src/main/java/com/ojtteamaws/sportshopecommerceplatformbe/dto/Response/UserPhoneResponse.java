package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPhoneResponse {

    private Long id;
    private String phoneNumber;
    private boolean defaultPhone;
}
