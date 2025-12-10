package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NavigationItemResponse {

    private Long id;                 // categoryId / brandId / sportId, hoặc null cho node static
    private String name;             // tên hiển thị
    private String slug;             // slug (category/brand/sport), có thể null
    private List<NavigationItemResponse> items; // children (sub-menu)
}
