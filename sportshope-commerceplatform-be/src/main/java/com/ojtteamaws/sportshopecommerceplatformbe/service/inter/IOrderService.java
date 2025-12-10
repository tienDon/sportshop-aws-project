package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CreateOrderRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.OrderDetailResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.OrderListResultResponse;

public interface IOrderService {

    OrderDetailResponse createOrder(Long userId, CreateOrderRequest request);

    OrderListResultResponse getOrders(Long userId, Integer page, Integer limit);

    OrderDetailResponse getOrderById(Long orderId, Long userId);
}
