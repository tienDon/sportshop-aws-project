package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NavigationSectionResponse {

    private String id;                          // ví dụ: nav-men, STATIC_BRAND
    private String name;                        // "Nam", "Thương Hiệu", ...
    private String slug;                        // "nam", "thuong-hieu", ...
    private List<NavigationItemResponse> children; // danh sách cột/menu con
}
