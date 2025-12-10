package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPhoneRequest {
    private String phoneNumber;     // số điện thoại phụ muốn thêm/sửa
    private Boolean defaultPhone;   // true = set làm default, null = không đổi
}
