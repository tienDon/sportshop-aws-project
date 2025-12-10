package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.AddToCartRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UpdateCartItemRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.CartResponse;

public interface ICartService {

    CartResponse getCart(Long userId);

    CartResponse addToCart(Long userId, AddToCartRequest request);

    CartResponse updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest request);

    CartResponse removeCartItem(Long userId, Long cartItemId);

    Long getCartItemCount(Long userId);
}
