package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.*;

// com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.AuthResponse

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String fullName;
    private boolean verified;
    // ➕ thêm 2 field này
    private String role;   // "ADMIN" / "USER" / "STAFF"...
    private Long userId;
}
