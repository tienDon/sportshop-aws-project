package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddressResponse {

    private Long id;
    private String addressDetail;
    private boolean defaultAddress;
}
