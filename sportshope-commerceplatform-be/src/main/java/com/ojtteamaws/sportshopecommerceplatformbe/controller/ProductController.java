package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.*;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ProductDetailResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ProductListResultResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ProductVariantAdminResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ProductVariant;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    // POST /api/products
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest request) {
        Long productId = productService.createProduct(request);
        return ResponseEntity.status(201).body(
                Map.of(
                        "success", true,
                        "message", "Product created successfully",
                        "data", Map.of("id", productId)
                )
        );
    }

    // GET /api/products
    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(value = "slugCategory", required = false) String slugCategory,
            @RequestParam(value = "slugAudience", required = false) String slugAudience,
            @RequestParam(value = "slugBrand", required = false) String slugBrand,
            @RequestParam(value = "slugSport", required = false) String slugSport,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "colorSlugs", required = false) String colorSlugs,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "page", required = false) Integer page
    ) {
        ProductListResultResponse result = productService.getProductsByQuery(
                slugCategory, slugAudience, slugBrand, slugSport,
                q, colorSlugs, minPrice, maxPrice, limit, page
        );
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", result.getProducts(),
                        "pagination", result.getPagination()
                )
        );
    }

    // GET /api/products/slug/{slug}

    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> getProductBySlug(@PathVariable String slug) {
        try {
            ProductDetailResponse product = productService.getProductBySlug(slug);

            List<ProductDetailResponse> data =
                    (product != null) ? List.of(product) : Collections.emptyList();

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "data", data
                    )
            );
        } catch (Exception ex) {
            // Nếu lỡ đâu bên dưới vẫn quăng exception (hoặc global handler đang map RuntimeException -> 403),
            // mình bắt lại tại đây và vẫn trả 200 + mảng rỗng.
            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "data", Collections.emptyList()
                    )
            );
        }
    }


    // POST /api/products/{id}/audiences
    @PostMapping("/{id}/audiences")
    public ResponseEntity<?> createProductAudience(
            @PathVariable("id") Long productId,
            @RequestBody ProductAudienceRequest request
    ) {
        if (request.getAudienceId() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "audienceId is required in the request body"
                    )
            );
        }
        productService.addAudienceToProduct(productId, request.getAudienceId());
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Audience added to product successfully"
                )
        );
    }

    // GET /api/products/{id}/audiences
    @GetMapping("/{id}/audiences")
    public ResponseEntity<?> getProductsAudience(@PathVariable("id") Long productId) {
        List<ProductDetailResponse.AudienceSummaryResponse> audiences =
                productService.getAudiencesByProductId(productId);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", audiences
                )
        );
    }


    // POST /api/products/{id}/categories
    @PostMapping("/{id}/categories")
    public ResponseEntity<?> createProductCategory(
            @PathVariable("id") Long productId,
            @RequestBody ProductCategoryRequest request
    ) {
        if (request.getCategoryId() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "categoryId is required in the request body"
                    )
            );
        }
        productService.addCategoryToProduct(
                productId,
                request.getCategoryId(),
                request.getIsPrimary()
        );
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Category added to product successfully"
                )
        );
    }

    // GET /api/products/{id}/categories
    @GetMapping("/{id}/categories")
    public ResponseEntity<?> getProductCategories(@PathVariable("id") Long productId) {

        List<ProductDetailResponse.CategorySummaryResponse> categories =
                productService.getCategoriesByProductId(productId);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Product categories fetched successfully",
                        "data", categories
                )
        );
    }


    // POST /api/products/{id}/sports
    @PostMapping("/{id}/sports")
    public ResponseEntity<?> createProductSport(
            @PathVariable("id") Long productId,
            @RequestBody ProductSportRequest request
    ) {
        if (request.getSportId() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "sportId is required in the request body"
                    )
            );
        }
        productService.addSportToProduct(productId, request.getSportId());
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Sport added to product successfully"
                )
        );
    }

    // POST /api/products/{id}/variants
    @PostMapping("/{id}/variants")
    public ResponseEntity<?> createProductVariant(
            @PathVariable("id") Long productId,
            @RequestBody ProductVariantRequest request
    ) {
        if (request.getColorId() == null ||
                request.getSizeId() == null ||
                request.getPrice() == null ||
                request.getStockQuantity() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "colorId, sizeId, price, and stockQuantity are required in the request body"
                    )
            );
        }

        ProductVariant variant = productService.addVariantToProduct(productId, request);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Variant added to product successfully",
                        "data", variant
                )
        );
    }

    // DELETE /api/products/{id}/variants/{variantId}
    @DeleteMapping("/{id}/variants/{variantId}")
    public ResponseEntity<?> deleteProductVariant(
            @PathVariable("id") Long productId,
            @PathVariable Long variantId
    ) {
        productService.deleteVariantFromProduct(productId, variantId);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Variant deleted successfully"
                )
        );
    }

    // GET /api/products/{id}/variants
    @GetMapping("/{id}/variants")
    public ResponseEntity<?> getProductVariants(@PathVariable("id") Long productId) {

        List<ProductVariantAdminResponse> variants =
                productService.getVariantsByProductId(productId);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", variants
                )
        );
    }


    // PATCH /api/products/{id}/variants/{variantId}
    @PatchMapping("/{id}/variants/{variantId}")
    public ResponseEntity<?> updateProductVariant(
            @PathVariable("id") Long productId,
            @PathVariable Long variantId,
            @RequestBody UpdateProductVariantRequest request
    ) {
        ProductVariant variant = productService.updateVariantOfProduct(productId, variantId, request);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Variant updated successfully",
                        "data", variant
                )
        );
    }

    // POST /api/products/{id}/attribute-values
    @PostMapping("/{id}/attribute-values")
    public ResponseEntity<?> createProductAttributeValue(
            @PathVariable("id") Long productId,
            @RequestBody ProductAttributeValueRequest request
    ) {
        if (request.getAttributeValueId() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "attributeValueId is required in the request body"
                    )
            );
        }

        productService.addAttributeValueToProduct(productId, request.getAttributeValueId());

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Attribute value added to product successfully"
                )
        );
    }
}
