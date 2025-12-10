package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductVariantResponse {

    private Integer id;
    private String sku;
    private BigDecimal price;
    private Integer stockQuantity;
    private Integer colorId;
    private String sizeName;
    private List<String> imageUrls; // map từ Json
}
