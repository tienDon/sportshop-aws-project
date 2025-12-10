package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.AddToCartRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UpdateCartItemRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;

    private Long getUserId(Principal principal) {
        return Long.parseLong(principal.getName());
    }

    @GetMapping
    public ResponseEntity<?> getCart(Principal principal) {
        Long userId = getUserId(principal);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", cartService.getCart(userId)
                )
        );
    }

    @PostMapping("/items")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request,
                                       Principal principal) {
        Long userId = getUserId(principal);
        System.out.println("User ID: " + userId);
        System.out.println("AddToCartRequest: " + request);
        System.out.println("Variant ID: " + request.getVariantId());
        System.out.println("Quantity: " + request.getQuantity());
        System.out.println("-----------------------");
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", cartService.addToCart(userId, request)
                )
        );
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long itemId,
                                            @RequestBody UpdateCartItemRequest request,
                                            Principal principal) {
        Long userId = getUserId(principal);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", cartService.updateCartItem(userId, itemId, request)
                )
        );
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long itemId,
                                            Principal principal) {
        Long userId = getUserId(principal);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", cartService.removeCartItem(userId, itemId)
                )
        );
    }

    @GetMapping("/items/count")
    public ResponseEntity<?> getCartItemCount(Principal principal) {
        Long userId = getUserId(principal);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", Map.of("count", cartService.getCartItemCount(userId))
                )
        );
    }
}

