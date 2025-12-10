package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCategoryRequest {

    private Long categoryId;
    private Boolean isPrimary;
}
