package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {

    private Long id;
    private Long userId;
    private Integer totalItems;
    private BigDecimal totalPrice;
    private List<CartItemResponse> items;

    @Data
    public static class CartItemResponse {
        private Long itemId;
        private Integer quantity;
        private Boolean isSelected;
        private ProductSummary product;
        private VariantSummary variant;
    }

    @Data
    public static class ProductSummary {
        private Long id;
        private String name;
        private String slug;
        private String brandName;
        private String mainImageUrl;
    }

    @Data
    public static class VariantSummary {
        private Long variantId;
        private String sku;
        private BigDecimal price;
        private Integer stockQuantity;
        private ColorSummary color;
        private SizeSummary size;
        private String image;
    }

    @Data
    public static class ColorSummary {
        private String name;
        private String hexCode;
    }

    @Data
    public static class SizeSummary {
        private String name;
    }
}
