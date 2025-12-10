package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductVariantAdminResponse {

    private Long id;
    private Long productId;
    private Long colorId;
    private Long sizeId;
    private BigDecimal price;
    private Integer stockQuantity;
    private String sku;
    private List<String> imageUrls;   // hoặc Object nếu bạn đang lưu JSON string

    private ProductSummary product;

    @Getter
    @Setter
    public static class ProductSummary {
        private Long id;
        private String name;
        private String slug;
        private Long brandId;
        private BigDecimal basePrice;
        private String mainImageUrl;
        private Boolean isActive;
        private String description;
        private String specifications;
        private String note;
        private Long badgeId;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;
    }
}
