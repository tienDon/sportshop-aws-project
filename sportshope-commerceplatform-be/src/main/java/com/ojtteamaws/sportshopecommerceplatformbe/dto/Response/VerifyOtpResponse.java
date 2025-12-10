// ⭐ NEW: Response cho API /api/auth/verify-otp
package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpResponse {

    private boolean success;
    private String message;

    // user đã login (theo dạng UserBasicResponse ở trên)
    private UserBasicResponse user;

    // accessToken JWT để FE lưu vào Zustand
    private String accessToken;

    // lỗi chi tiết nếu fail
    private String error;
    private String refreshToken;
}
