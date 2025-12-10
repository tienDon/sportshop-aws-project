package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UpdateProductVariantRequest {

    private Long colorId;          // optional
    private Long sizeId;           // optional
    private BigDecimal price;      // optional
    private Integer stockQuantity; // optional
    private String sku;            // optional
    private List<String> imageUrls;// optional
}
