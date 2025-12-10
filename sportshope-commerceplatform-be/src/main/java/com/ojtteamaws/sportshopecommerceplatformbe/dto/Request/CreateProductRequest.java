package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {

    private String name;
    private String slug;
    private Long brandId;
    private BigDecimal basePrice;
    private String mainImageUrl;
    private Boolean isActive;
    private String description;
    private String specifications;
    private String note;
    private Long badgeId;
}
