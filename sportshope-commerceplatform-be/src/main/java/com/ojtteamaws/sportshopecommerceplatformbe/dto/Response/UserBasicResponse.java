// ⭐ NEW: User cơ bản trả về cho FE (trong VerifyOtpResponse, /me, v.v.)
package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserBasicResponse {

    // id user trong DB (map từ entity User.id)
    private Long id;

    // tên hiển thị (map từ fullName)
    private String name;
    private String email;
    private String phone;

    // Nếu sau này bạn có avatar / địa chỉ có thể map thêm
    private String avatarUrl;
    private String address;
    private String role;

    // hai field này optional – nếu bạn chưa có thì có thể để null
    private Instant createdAt;
    private Instant updatedAt;
}
