package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemSummaryResponse {
    private Integer quantity;
    private BigDecimal price;
    private String productName;
    private String variantDetails;
    private String mainImageUrl;
}
