package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.OtpRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.VerifyOtpRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.ResendOtpRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.RequestOtpResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.VerifyOtpResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ResendOtpResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserBasicResponse;

public interface IAuthService {

    // REQUEST OTP (signup + signin)
    RequestOtpResponse requestOtp(OtpRequest request);

    // VERIFY OTP
    VerifyOtpResponse verifyOtp(VerifyOtpRequest request);

    // RESEND OTP
    ResendOtpResponse resendOtp(ResendOtpRequest request);

    // REFRESH ACCESS TOKEN: trả về accessToken mới (hoặc ném exception nếu invalid)
    String refreshAccessToken(String refreshToken);

    // LẤY USER HIỆN TẠI theo id
    UserBasicResponse getCurrentUser(Long userId);
}
