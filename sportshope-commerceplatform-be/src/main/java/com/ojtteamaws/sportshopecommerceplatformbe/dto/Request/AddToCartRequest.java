package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long variantId;
    private Integer quantity;
}
