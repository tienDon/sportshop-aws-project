package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductListResultResponse {

    private List<ProductListItemResponse> products;
    private PaginationResponse pagination;
}
