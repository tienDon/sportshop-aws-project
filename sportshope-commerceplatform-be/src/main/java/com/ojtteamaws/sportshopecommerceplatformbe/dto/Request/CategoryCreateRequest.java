package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateRequest {
    private String name;
    private String slug;
    private Long parentId; // nullable
}
