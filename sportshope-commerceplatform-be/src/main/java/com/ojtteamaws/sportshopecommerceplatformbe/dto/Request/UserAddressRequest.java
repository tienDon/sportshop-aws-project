package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddressRequest {

    // Địa chỉ đầy đủ (1 field duy nhất)
    private String addressDetail;
    private Boolean defaultAddress;   // true = set làm default, null = không đổi
}
