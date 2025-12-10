// ⭐ NEW: DTO cho API /api/auth/request-otp
package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpRequest {

    /**
     * identifier = email hoặc phone (chuỗi FE gửi lên)
     * VD: "abc@gmail.com" hoặc "0367xxxxxx"
     */
    private String identifier;

    /**
     * name chỉ dùng khi SIGNUP (đăng ký mới).
     * Nếu LOGIN thì FE không gửi field này (null).
     */
    private String name; // optional
}
