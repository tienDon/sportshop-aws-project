package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.OtpRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.VerifyOtpRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.ResendOtpRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.RequestOtpResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.VerifyOtpResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ResendOtpResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserBasicResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.IUserRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.security.JWTProvider;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IAuthService;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.model.MessageRejectedException;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    // ⭐ SES email OTP
    private final OtpService otpService;

    // ⭐ SNS phone OTP (giữ nguyên impl cũ của bạn)
    private final LocalPhoneOtpServiceImpl phoneOtpService;

    private final IUserRepository userRepository;
    private final JWTProvider jwtProvider;

    // ========================================================================
    // 1) REQUEST OTP (SIGNUP + SIGNIN)
    // ========================================================================
    @Override
    public RequestOtpResponse requestOtp(OtpRequest request) {
        String identifier = request.getIdentifier().trim();
        boolean isEmail = identifier.contains("@");

        RequestOtpResponse resp = new RequestOtpResponse();

        // 1. Tìm user theo email/phone
        Optional<User> optUser = isEmail
                ? userRepository.findByEmail(identifier)
                : userRepository.findByPhone(identifier);

        User user = optUser.orElse(null);

        // 2. Nếu chưa có user → chỉ cho phép nếu FE gửi kèm name (signup)
        if (user == null) {
            if (request.getName() == null || request.getName().isBlank()) {
                resp.setSuccess(false);
                resp.setMessage("Tài khoản chưa tồn tại. Vui lòng đăng ký (cần truyền kèm name).");
                resp.setOtpToken(null);
                resp.setExpiresAt(null);
                return resp;
            }

            // ⭐ NEW: tạo user mới cho flow đăng ký OTP
            user = User.builder()
                    .fullName(request.getName())
                    .email(isEmail ? identifier : null)
                    .phone(isEmail ? null : identifier)
                    .role("USER")  // admin seed sẵn
                    .build();
            userRepository.save(user);
        }

        // 3. Gửi OTP
        try {
            if (isEmail) {
                otpService.sendEmailOtp(identifier);        // SES
            } else {
                phoneOtpService.sendPhoneOtp(identifier);   // SNS
            }
        } catch (MessageRejectedException e) {
            // ⭐ Lỗi SES: email chưa verify, hoặc bị chặn
            resp.setSuccess(false);
            resp.setMessage("Không thể gửi OTP qua email: " + e.awsErrorDetails().errorMessage());
            resp.setOtpToken(null);
            resp.setExpiresAt(null);
            return resp;
        } catch (Exception e) {
            // ⭐ Các lỗi khác (network, config...)
            resp.setSuccess(false);
            resp.setMessage("Không thể gửi OTP. Vui lòng thử lại sau.");
            resp.setOtpToken(null);
            resp.setExpiresAt(null);
            return resp;
        }

        // 4. Build response OK
        resp.setSuccess(true);
        resp.setMessage("Đã gửi mã OTP. Vui lòng kiểm tra " + (isEmail ? "email" : "số điện thoại") + " của bạn.");
        resp.setOtpToken(identifier);
        resp.setExpiresAt(Instant.now().plusSeconds(5 * 60));
        return resp;
    }

    // ========================================================================
    // 2) VERIFY OTP
    // ========================================================================
    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
        String identifier = request.getOtpToken().trim();  // chính là email/phone
        String otpCode = request.getOtpCode().trim();
        boolean isEmail = identifier.contains("@");

        boolean ok;
        if (isEmail) {
            ok = otpService.verifyEmailOtp(identifier, otpCode);
        } else {
            ok = phoneOtpService.verifyPhoneOtp(identifier, otpCode);
        }

        VerifyOtpResponse resp = new VerifyOtpResponse();

        if (!ok) {
            resp.setSuccess(false);
            resp.setMessage("Xác thực OTP thất bại. Mã không đúng hoặc đã hết hạn.");
            resp.setAccessToken(null);
            resp.setUser(null);
            resp.setRefreshToken(null);
            return resp;
        }

        // OTP OK → lấy user
        User user = isEmail
                ? userRepository.findByEmail(identifier)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + identifier))
                : userRepository.findByPhone(identifier)
                .orElseThrow(() -> new RuntimeException("User not found with phone: " + identifier));

        String subject = user.getId().toString();

        // ⭐ accessToken + refreshToken
        String accessToken = jwtProvider.generateAccessToken(subject);
        String refreshToken = jwtProvider.generateRefreshToken(subject);

        // Build user DTO cho FE
        UserBasicResponse userDto = new UserBasicResponse();
        userDto.setId(user.getId());
        userDto.setName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole());

        resp.setSuccess(true);
        resp.setMessage("Xác thực OTP thành công!");
        resp.setAccessToken(accessToken);
        resp.setRefreshToken(refreshToken);  // ⭐ Controller sẽ lấy để set cookie
        resp.setUser(userDto);

        return resp;
    }

    // ========================================================================
    // 3) RESEND OTP
    // ========================================================================
    @Override
    public ResendOtpResponse resendOtp(ResendOtpRequest request) {
        String identifier = request.getOtpToken().trim();
        boolean isEmail = identifier.contains("@");

        if (isEmail) {
            otpService.sendEmailOtp(identifier);
        } else {
            phoneOtpService.sendPhoneOtp(identifier);
        }

        ResendOtpResponse resp = new ResendOtpResponse();
        resp.setSuccess(true);
        resp.setMessage("Đã gửi lại mã OTP.");
        resp.setOtpToken(identifier);
        resp.setExpiresAt(Instant.now().plusSeconds(5 * 60));

        return resp;
    }

    // ========================================================================
    // 4) REFRESH TOKEN
    // ========================================================================
    @Override
    public String refreshAccessToken(String refreshToken) {
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token không hợp lệ hoặc đã hết hạn");
        }

        String userId = jwtProvider.extractSubject(refreshToken);
        return jwtProvider.generateAccessToken(userId);
    }

    // ========================================================================
    // 5) LẤY USER HIỆN TẠI
    // ========================================================================
    @Override
    public UserBasicResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        UserBasicResponse userDto = new UserBasicResponse();
        userDto.setId(user.getId());
        userDto.setName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole());

        return userDto;
    }
}
