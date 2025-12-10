package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BrandSummaryResponse {
    private Integer id;
    private String name;
    private String slug;
}

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class SimpleCategoryResponse {
    private Integer id;
    private String name;
    private String slug;
}

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class SimpleAudienceResponse {
    private Integer id;
    private String name;
    private String slug;
}

@Data @NoArgsConstructor @AllArgsConstructor @Builder
class SimpleSportResponse {
    private Integer id;
    private String name;
    private String slug;
}
