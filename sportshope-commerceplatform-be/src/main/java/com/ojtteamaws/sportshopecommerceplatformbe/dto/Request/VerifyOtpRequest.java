// ⭐ NEW: DTO cho API /api/auth/verify-otp
package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequest {

    // token server trả về ở bước request-otp
    private String otpToken;
    // mã OTP user nhập (6 số chẳng hạn)
    private String otpCode;
}
