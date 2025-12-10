package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CreateOrderRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    private Long getUserId(Principal principal) {
        // giống UserProfileController: JwtAuthFilter set principal = userId.toString()
        return Long.parseLong(principal.getName());
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request,
                                         Principal principal) {
        Long userId = getUserId(principal);
        return ResponseEntity.status(201).body(
                Map.of(
                        "success", true,
                        "data", orderService.createOrder(userId, request)
                )
        );
    }

    @GetMapping
    public ResponseEntity<?> getOrders(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit,
            Principal principal
    ) {
        Long userId = getUserId(principal);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", orderService.getOrders(userId, page, limit)
                )
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId,
                                          Principal principal) {
        Long userId = getUserId(principal);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", orderService.getOrderById(orderId, userId)
                )
        );
    }
}
