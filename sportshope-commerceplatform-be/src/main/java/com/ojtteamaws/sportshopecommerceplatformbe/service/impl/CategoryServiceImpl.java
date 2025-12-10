package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CategoryCreateRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CategoryAudiencesRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CategoryAttributesRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.CategoryTreeNodeResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.CategoryAttributeResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.*;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.*;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final AudienceRepository audienceRepository;
    private final AttributeRepository attributeRepository;
    private final CategoryAudienceRepository categoryAudienceRepository;
    private final CategoryAttributeRepository categoryAttributeRepository;


    @Override
    public CategoryTreeNodeResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug).orElse(null);

        if (category == null) {
            return null;  // KHÔNG throw exception để navigation không bị crash
        }

        return mapToTreeNode(category); // dùng hàm map category -> tree node
    }


    @Override
    public CategoryTreeNodeResponse createCategory(CategoryCreateRequest request) {
        if (request.getName() == null || request.getSlug() == null) {
            throw new IllegalArgumentException("Name and slug are required");
        }

        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
        } else {
            // giống logic Prisma: check trùng name/slug ở root
            if (categoryRepository.existsByNameAndParentIsNull(request.getName())) {
                throw new RuntimeException("Category with same name at root already exists");
            }
            if (categoryRepository.existsBySlugAndParentIsNull(request.getSlug())) {
                throw new RuntimeException("Category with same slug at root already exists");
            }
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setParent(parent);
        category.setIsActive(true);

        Category saved = categoryRepository.save(category);
        return toTreeNode(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTreeNodeResponse> getCategoryTree() {
        List<Category> roots = categoryRepository.findByParentIsNull();
        List<CategoryTreeNodeResponse> result = new ArrayList<>();
        for (Category c : roots) {
            result.add(toTreeNodeRecursive(c));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryTreeNodeResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);

        // Không tìm thấy thì trả null, để chỗ gọi tự xử lý
        if (category == null) {
            return null;
        }

        return toTreeNodeRecursive(category);
    }


    @Override
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }

    @Override
    public void addAudiencesToCategory(Long categoryId, CategoryAudiencesRequest request) {
        if (request.getAudienceIds() == null || request.getAudienceIds().isEmpty()) {
            throw new IllegalArgumentException("audienceIds must not be empty");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        int sortOrder = request.getSortOrder() != null ? request.getSortOrder() : 0;

        for (Long audienceId : request.getAudienceIds()) {
            Audience audience = audienceRepository.findById(audienceId)
                    .orElseThrow(() -> new RuntimeException("Audience not found: " + audienceId));

            boolean exists = categoryAudienceRepository.existsByCategoryAndAudience(category, audience);
            if (exists) {
                continue; // giống skipDuplicates
            }

            CategoryAudience ca = new CategoryAudience();
            ca.setCategory(category);
            ca.setAudience(audience);
            ca.setSortOrder(sortOrder);
            categoryAudienceRepository.save(ca);
        }
    }

    @Override
    public void addAttributesToCategory(Long categoryId, CategoryAttributesRequest request) {
        if (request.getAttributeIds() == null || request.getAttributeIds().isEmpty()) {
            throw new IllegalArgumentException("attributeIds must not be empty");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        for (Long attributeId : request.getAttributeIds()) {
            Attribute attribute = attributeRepository.findById(attributeId)
                    .orElseThrow(() -> new RuntimeException("Attribute not found: " + attributeId));

            // có thể check duplicate tuỳ ý, ở đây cho đơn giản: luôn tạo mới
            CategoryAttribute ca = new CategoryAttribute();
            ca.setCategory(category);
            ca.setAttribute(attribute);
            categoryAttributeRepository.save(ca);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTreeNodeResponse> getCategoryByAudienceSlug(String slug) {
        Audience audience = audienceRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Audience not found"));

        List<Category> categories = categoryRepository.findRootByAudienceSlug(audience.getSlug());
        List<CategoryTreeNodeResponse> result = new ArrayList<>();
        for (Category c : categories) {
            result.add(toTreeNodeRecursive(c));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryAttributeResponse> getCategoryAttributes(Long categoryId) {
        List<CategoryAttribute> list = categoryAttributeRepository.findByCategory_Id(categoryId);
        List<CategoryAttributeResponse> result = new ArrayList<>();

        for (CategoryAttribute ca : list) {
            Attribute attr = ca.getAttribute();
            CategoryAttributeResponse dto = new CategoryAttributeResponse();
            dto.setId(attr.getId());
            dto.setName(attr.getName());
            dto.setCode(attr.getCode());
            result.add(dto);
        }
        return result;
    }

    // ================== Helper mapping ==================

    private CategoryTreeNodeResponse toTreeNode(Category category) {
        CategoryTreeNodeResponse dto = new CategoryTreeNodeResponse();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setChildren(new ArrayList<>());
        return dto;
    }

    private CategoryTreeNodeResponse toTreeNodeRecursive(Category category) {
        CategoryTreeNodeResponse dto = toTreeNode(category);
        List<CategoryTreeNodeResponse> childDtos = new ArrayList<>();
        if (category.getChildren() != null) {
            for (Category child : category.getChildren()) {
                childDtos.add(toTreeNodeRecursive(child));
            }
        }
        dto.setChildren(childDtos);
        return dto;
    }
    private CategoryTreeNodeResponse mapToTreeNode(Category category) {

        CategoryTreeNodeResponse node = new CategoryTreeNodeResponse();
        node.setId(category.getId());
        node.setName(category.getName());
        node.setSlug(category.getSlug());

        List<CategoryTreeNodeResponse> children = new ArrayList<>();
        if (category.getChildren() != null) {
            for (Category child : category.getChildren()) {
                children.add(mapToTreeNode(child));
            }
        }
        node.setChildren(children);

        return node;
    }

}
