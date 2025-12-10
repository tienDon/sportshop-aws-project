package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductVariantRequest {

    private Long colorId;
    private Long sizeId;
    private BigDecimal price;
    private Integer stockQuantity;
    private String sku;
    private List<String> imageUrls; // chỉ dùng khi update
}
