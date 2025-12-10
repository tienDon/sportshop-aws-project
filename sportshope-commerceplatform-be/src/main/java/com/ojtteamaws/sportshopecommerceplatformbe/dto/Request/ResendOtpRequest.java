// ⭐ NEW: DTO cho API /api/auth/resend-otp
package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendOtpRequest {

    // token hiện tại của OTP, dùng để biết resend cho email/phone nào
    private String otpToken;
}
