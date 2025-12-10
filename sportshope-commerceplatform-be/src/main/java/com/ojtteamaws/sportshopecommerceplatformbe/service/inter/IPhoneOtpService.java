package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

public interface IPhoneOtpService {
    void sendPhoneOtp(String phone);
    boolean verifyPhoneOtp(String phone, String otp);
}
