package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;


import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderListItemResponse {
    private Long orderId;
    private String orderCode;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal totalFinalAmount;
    private String receiverName;
    private String shippingAddress;
    private List<OrderItemSummaryResponse> items;
}
