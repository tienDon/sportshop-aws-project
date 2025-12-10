package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CategoryCreateRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CategoryAudiencesRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CategoryAttributesRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.CategoryTreeNodeResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.CategoryAttributeResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/tree")
    public ResponseEntity<?> getCategoryTree() {
        List<CategoryTreeNodeResponse> data = categoryService.getCategoryTree();
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Category tree fetched successfully",
                        "data", data
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryCreateRequest request) {
        CategoryTreeNodeResponse category = categoryService.createCategory(request);
        return ResponseEntity.status(201).body(
                Map.of(
                        "success", true,
                        "message", "Category created successfully",
                        "data", category
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        CategoryTreeNodeResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Category fetched successfully",
                        "data", category
                )
        );
    }

    // WARNING: giống Node – chỉ dùng khi test
    @DeleteMapping
    public ResponseEntity<?> deleteAllCategories() {
        categoryService.deleteAllCategories();
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "All categories deleted successfully"
                )
        );
    }

    @PostMapping("/{categoryId}/audiences")
    public ResponseEntity<?> createCategoryAudiences(
            @PathVariable Long categoryId,
            @RequestBody CategoryAudiencesRequest request
    ) {
        categoryService.addAudiencesToCategory(categoryId, request);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Audiences added to category successfully"
                )
        );
    }

    @GetMapping("/audiences/{slug}")
    public ResponseEntity<?> getCategoryByAudienceSlug(@PathVariable String slug) {
        List<CategoryTreeNodeResponse> categories = categoryService.getCategoryByAudienceSlug(slug);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Category fetched successfully",
                        "data", categories
                )
        );
    }

    @PostMapping("/{categoryId}/attributes")
    public ResponseEntity<?> createCategoryAttributes(
            @PathVariable Long categoryId,
            @RequestBody CategoryAttributesRequest request
    ) {
        categoryService.addAttributesToCategory(categoryId, request);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Attributes added to category successfully"
                )
        );
    }

    @GetMapping("/{categoryId}/attributes")
    public ResponseEntity<?> getCategoryAttributes(@PathVariable Long categoryId) {
        List<CategoryAttributeResponse> attributes = categoryService.getCategoryAttributes(categoryId);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Category attributes fetched successfully",
                        "data", attributes
                )
        );
    }
}
