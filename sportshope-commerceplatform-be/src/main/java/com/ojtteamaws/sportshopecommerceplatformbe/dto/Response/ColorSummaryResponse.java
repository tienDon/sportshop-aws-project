package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ColorSummaryResponse {
    private Integer id;
    private String name;
    private String hexCode;
}
