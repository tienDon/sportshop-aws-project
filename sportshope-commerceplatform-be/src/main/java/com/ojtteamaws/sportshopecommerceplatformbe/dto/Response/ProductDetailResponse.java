package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProductDetailResponse {

    private Long id;
    private String name;
    private String slug;

    private BrandSummary brand;   // {id, name, slug} như Node
    private String brandName;     // để giữ backward compatibility

    private BigDecimal basePrice;
    private String description;
    private String specifications;
    private String note;

    private List<ColorResponse> colors;          // list unique màu
    private List<String> sizes;                  // list unique size name
    private List<ProductAttributeGroupResponse> attributes;
    private List<ProductVariantResponse> variants;

    private List<CategorySummaryResponse> categories;
    private List<AudienceSummaryResponse> audiences;
    private List<SportSummaryResponse> sports;

    @Getter
    @Setter
    public static class BrandSummary {
        private Long id;
        private String name;
        private String slug;
    }

    @Getter
    @Setter
    public static class ColorResponse {
        private Long id;
        private String name;
        private String hexCode;
    }

    @Getter
    @Setter
    public static class ProductAttributeGroupResponse {
        private String name;
        private List<String> value;
    }

    @Getter
    @Setter
    public static class ProductVariantResponse {
        private Long id;
        private String sku;
        private BigDecimal price;
        private int stockQuantity;
        private Long colorId;
        private String sizeName;
        private Object imageUrls; // Json -> có thể để Object / String (JSON) tuỳ bạn
    }

    @Getter
    @Setter
    public static class CategorySummaryResponse {
        private Long id;
        private Long parentId;
        private String name;
        private String slug;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    public static class AudienceSummaryResponse {
        private Long id;
        private String name;
        private String slug;
        private Integer sortOrder;
    }

    @Getter
    @Setter
    public static class SportSummaryResponse {
        private Long id;
        private String name;
        private String slug;
    }
}
