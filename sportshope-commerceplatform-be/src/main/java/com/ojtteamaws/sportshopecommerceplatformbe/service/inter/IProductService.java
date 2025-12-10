package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.*;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ProductDetailResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ProductListResultResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ProductVariantAdminResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ProductVariant;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {

    // create product
    Long createProduct(CreateProductRequest request);

    // GET /api/products
    ProductListResultResponse getProductsByQuery(
            String slugCategory,
            String slugAudience,
            String slugBrand,
            String slugSport,
            String q,
            String colorSlugs,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer limit,
            Integer page
    );

    // /:id/audiences
    void addAudienceToProduct(Long productId, Long audienceId);

    // /:id/categories
    void addCategoryToProduct(Long productId, Long categoryId, Boolean isPrimary);

    // /:id/sports
    void addSportToProduct(Long productId, Long sportId);

    // /:id/variants (POST)
    ProductVariant addVariantToProduct(Long productId, ProductVariantRequest request);

    // DELETE /:id/variants/:variantId
    void deleteVariantFromProduct(Long productId, Long variantId);

    // GET /:id/variants

    // PATCH /:id/variants/:variantId
    ProductVariant updateVariantOfProduct(Long productId, Long variantId, UpdateProductVariantRequest request);

    // /:id/attribute-values
    void addAttributeValueToProduct(Long productId, Long attributeValueId);

    // GET /:id/audiences
    List<ProductDetailResponse.AudienceSummaryResponse> getAudiencesByProductId(Long productId);

    // GET /slug/:slug
    ProductDetailResponse getProductBySlug(String slug);
    List<ProductDetailResponse.CategorySummaryResponse> getCategoriesByProductId(Long productId);

    List<ProductVariantAdminResponse> getVariantsByProductId(Long productId);

}


