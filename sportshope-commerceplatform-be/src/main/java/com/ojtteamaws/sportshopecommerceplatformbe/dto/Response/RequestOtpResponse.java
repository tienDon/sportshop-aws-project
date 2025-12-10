// ⭐ NEW: Response cho API /api/auth/request-otp
package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RequestOtpResponse {

    private boolean success;
    private String message;

    // token nội bộ để verify / resend (không phải mã OTP người dùng nhập)
    private String otpToken;

    // thời điểm OTP hết hạn (FE hiển thị countdown nếu muốn)
    private Instant expiresAt;

    // nếu có lỗi thì fill vào đây, còn success=true thì để null
    private String error;
}
