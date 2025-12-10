package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Data;

import java.util.List;

@Data
public class OrderListResultResponse {
    private List<OrderListItemResponse> orders;
    private PaginationResponse pagination;
}
