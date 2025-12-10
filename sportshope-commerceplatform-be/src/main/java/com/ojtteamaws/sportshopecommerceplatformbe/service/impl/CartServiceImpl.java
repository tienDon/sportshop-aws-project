package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.AddToCartRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.UpdateCartItemRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.CartResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.*;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.*;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements ICartService {

    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepository;
    private final IProductVariantRepository productVariantRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        Cart cart = cartRepository.findByUser_Id(userId).orElse(null);

        if (cart == null) {
            // nếu chưa có cart thì tạo mới
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        return transformCart(cart);
    }

    @Override
    public CartResponse addToCart(Long userId, AddToCartRequest request) {
        ProductVariant variant = productVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        Cart cart = cartRepository.findByUser_Id(userId).orElse(null);
        if (cart == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        CartItem existingItem = cartItemRepository
                .findByCart_IdAndVariant_Id(cart.getId(), variant.getId())
                .orElse(null);

        int currentQty = existingItem != null ? existingItem.getQuantity() : 0;
        int newTotalQty = currentQty + request.getQuantity();

        if (newTotalQty > variant.getStockQuantity()) {
            throw new RuntimeException("Không đủ hàng. Chỉ còn " + variant.getStockQuantity() + " sản phẩm.");
        }

        if (existingItem != null) {
            existingItem.setQuantity(newTotalQty);
            cartItemRepository.save(existingItem);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setVariant(variant);
            item.setProduct(variant.getProduct());
            item.setQuantity(request.getQuantity());
            item.setIsSelected(true);
            cartItemRepository.save(item);
        }

        // reload cart
        Cart reload = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return transformCart(reload);
    }

    @Override
    public CartResponse updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cartItemRepository
                .findByIdAndCart_Id(cartItemId, cart.getId())
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        ProductVariant variant = item.getVariant();
        int qty = request.getQuantity();

        if (qty <= 0) {
            cartItemRepository.delete(item);
        } else {
            if (qty > variant.getStockQuantity()) {
                throw new RuntimeException("Không đủ hàng. Chỉ còn " + variant.getStockQuantity() + " sản phẩm.");
            }
            item.setQuantity(qty);
            cartItemRepository.save(item);
        }

        Cart reload = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return transformCart(reload);
    }

    @Override
    public CartResponse removeCartItem(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cartItemRepository
                .findByIdAndCart_Id(cartItemId, cart.getId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        cartItemRepository.delete(item);

        Cart reload = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return transformCart(reload);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCartItemCount(Long userId) {
        Cart cart = cartRepository.findByUser_Id(userId).orElse(null);
        if (cart == null) {
            return 0L;
        }
        return cartItemRepository.countByCart_Id(cart.getId());
    }

    // ---------- private helper: transformCart giống bên Node ----------

    private CartResponse transformCart(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUser() != null ? cart.getUser().getId() : null);

        List<CartResponse.CartItemResponse> itemDtos = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.ZERO;

        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                CartResponse.CartItemResponse itemDto = new CartResponse.CartItemResponse();
                itemDto.setItemId(item.getId());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setIsSelected(item.getIsSelected() != null ? item.getIsSelected() : true);

                Product product = item.getProduct();
                ProductVariant variant = item.getVariant();

                // product summary
                CartResponse.ProductSummary pDto = new CartResponse.ProductSummary();
                pDto.setId(product.getId());
                pDto.setName(product.getName());
                pDto.setSlug(product.getSlug());
                pDto.setMainImageUrl(product.getMainImageUrl());
                pDto.setBrandName(
                        product.getBrand() != null ? product.getBrand().getName() : ""
                );
                itemDto.setProduct(pDto);

                // variant summary
                CartResponse.VariantSummary vDto = new CartResponse.VariantSummary();
                vDto.setVariantId(variant.getId());
                vDto.setSku(variant.getSku());
                BigDecimal price = variant.getPrice() != null
                        ? variant.getPrice()
                        : product.getBasePrice();
                vDto.setPrice(price);
                vDto.setStockQuantity(variant.getStockQuantity());

                // color
                if (variant.getColor() != null) {
                    CartResponse.ColorSummary cDto = new CartResponse.ColorSummary();
                    cDto.setName(variant.getColor().getName());
                    cDto.setHexCode(variant.getColor().getHexCode());
                    vDto.setColor(cDto);
                }

                // size
                if (variant.getSize() != null) {
                    CartResponse.SizeSummary sDto = new CartResponse.SizeSummary();
                    sDto.setName(variant.getSize().getName());
                    vDto.setSize(sDto);
                }

                // image (ưu tiên imageUrls của variant, nếu null thì mainImageUrl)
                String image = null;
                if (variant.getImageUrls() != null && !variant.getImageUrls().isEmpty()) {
                    // giả sử imageUrls là List<String>; nếu là String JSON thì bạn tự parse lại
                    image = variant.getImageUrls().get(0);
                } else {
                    image = product.getMainImageUrl();
                }
                vDto.setImage(image);

                itemDto.setVariant(vDto);

                if (itemDto.getIsSelected()) {
                    totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
                }

                itemDtos.add(itemDto);
            }
        }

        response.setItems(itemDtos);
        response.setTotalItems(itemDtos.size());
        response.setTotalPrice(totalPrice);

        return response;
    }
}
