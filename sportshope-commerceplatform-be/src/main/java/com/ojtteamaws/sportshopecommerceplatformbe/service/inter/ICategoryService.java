package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CategoryCreateRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CategoryAudiencesRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CategoryAttributesRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.CategoryTreeNodeResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.CategoryAttributeResponse;

import java.util.List;

public interface ICategoryService {

    CategoryTreeNodeResponse createCategory(CategoryCreateRequest request);

    List<CategoryTreeNodeResponse> getCategoryTree();

    CategoryTreeNodeResponse getCategoryById(Long id);

    void deleteAllCategories();

    void addAudiencesToCategory(Long categoryId, CategoryAudiencesRequest request);

    void addAttributesToCategory(Long categoryId, CategoryAttributesRequest request);

    List<CategoryTreeNodeResponse> getCategoryByAudienceSlug(String slug);

    List<CategoryAttributeResponse> getCategoryAttributes(Long categoryId);
    CategoryTreeNodeResponse getCategoryBySlug(String slug);

}
