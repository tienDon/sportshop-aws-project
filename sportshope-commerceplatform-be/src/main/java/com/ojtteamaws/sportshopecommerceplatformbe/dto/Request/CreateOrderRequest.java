package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long cartId;          // id giỏ hàng muốn checkout
    private Long userAddressId;   // id của UserAddress
    private Long userPhoneId;     // id của UserPhone
    private String note;          // ghi chú đơn hàng
}
