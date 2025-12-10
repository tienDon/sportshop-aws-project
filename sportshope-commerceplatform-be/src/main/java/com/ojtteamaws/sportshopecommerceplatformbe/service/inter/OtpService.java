package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

public interface OtpService {
    void sendEmailOtp(String email);
    boolean verifyEmailOtp(String email, String otp);

}
