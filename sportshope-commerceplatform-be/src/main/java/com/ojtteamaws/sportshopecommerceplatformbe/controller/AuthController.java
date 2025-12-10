package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.OtpRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.VerifyOtpRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.ResendOtpRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.RequestOtpResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.VerifyOtpResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ResendOtpResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.UserBasicResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IAuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // ⭐ NEW: chỉ inject service, không inject repo / OTP / JWT trực tiếp
    private final IAuthService authService;

    // ========================================================================
    // 1) REQUEST OTP – /api/auth/request-otp
    // ========================================================================
    @PostMapping("/request-otp")
    public ResponseEntity<RequestOtpResponse> requestOtp(
            @Valid @RequestBody OtpRequest request
    ) {
        RequestOtpResponse resp = authService.requestOtp(request);
        HttpStatus status = resp.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    // ========================================================================
    // 2) VERIFY OTP – /api/auth/verify-otp
    // ========================================================================
    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request,
            HttpServletResponse servletResponse
    ) {
        VerifyOtpResponse resp = authService.verifyOtp(request);

        if (!resp.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        // ⭐ Set refreshToken vào HttpOnly cookie
        String refreshToken = resp.getRefreshToken();
        if (refreshToken != null && !refreshToken.isBlank()) {
            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)      // TODO: lên HTTPS thì chỉnh true
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .sameSite("Lax")
                    .build();
            servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        // FE chỉ cần accessToken + user, không cần refreshToken trong body cũng được,
        // nhưng giữ lại cũng không sao (TS phía FE bỏ qua field dư).
        return ResponseEntity.ok(resp);
    }

    // ========================================================================
    // 3) RESEND OTP – /api/auth/resend-otp
    // ========================================================================
    @PostMapping("/resend-otp")
    public ResponseEntity<ResendOtpResponse> resendOtp(
            @Valid @RequestBody ResendOtpRequest request
    ) {
        ResendOtpResponse resp = authService.resendOtp(request);
        HttpStatus status = resp.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    // ========================================================================
    // 4) REFRESH TOKEN – /api/auth/refresh-token
    // ========================================================================
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        try {
            String newAccessToken = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(new Object() {
                public final boolean success = true;
                public final String accessToken = newAccessToken;
            });
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Object() {
                        public final boolean success = false;
                        public final String message = "Refresh token không hợp lệ hoặc đã hết hạn";
                    });
        }
    }

    // ========================================================================
    // 5) LẤY USER HIỆN TẠI – /api/auth/me
    // ========================================================================
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Object() {
                        public final boolean success = false;
                        public final String message = "Chưa đăng nhập";
                    });
        }

        // JwtAuthFilter đang set principal = userId.toString()
        String userIdStr = (String) authentication.getPrincipal();
        Long userId = Long.parseLong(userIdStr);

        UserBasicResponse userDto = authService.getCurrentUser(userId);

        return ResponseEntity.ok(new Object() {
            public final boolean success = true;
            public final UserBasicResponse user = userDto;
        });
    }

    // ========================================================================
    // 6) LOGOUT – /api/auth/logout
    // ========================================================================
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse servletResponse) {
        // Xoá cookie refreshToken
        ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        servletResponse.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());

        return ResponseEntity.ok(new Object() {
            public final boolean success = true;
            public final String message = "Đăng xuất thành công!";
        });
    }

    // ========================================================================
    // ⭐ LEGACY NOTE:
    // Các flow /register, /login, password, Cognito... đã bỏ.
    // Nếu không còn chỗ nào sử dụng:
    //  - Xóa các DTO cũ: LoginRequest, RegisterRequest, Phone*Password*Request,...
    //
    //  - Xóa JwtAuthService cũ nếu không dùng.
    // ========================================================================
}
