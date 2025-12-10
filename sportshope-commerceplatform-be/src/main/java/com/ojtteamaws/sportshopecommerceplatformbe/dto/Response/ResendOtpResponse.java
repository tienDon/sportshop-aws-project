// ⭐ NEW: Response cho API /api/auth/resend-otp
package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResendOtpResponse {

    private boolean success;
    private String message;

    // token mới (sau khi resend OTP)
    private String otpToken;
    private Instant expiresAt;

    private String error;
}
