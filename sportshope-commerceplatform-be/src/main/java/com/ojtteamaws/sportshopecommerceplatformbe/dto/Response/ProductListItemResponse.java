package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductListItemResponse {

    private Long id;
    private String name;
    private String slug;
    private String mainImageUrl;
    private BigDecimal basePrice;
    private Long badgeId;
    private String brandName;
    private List<String> colors; // hexCode list
    private List<String> sizes;  // size name list
}
