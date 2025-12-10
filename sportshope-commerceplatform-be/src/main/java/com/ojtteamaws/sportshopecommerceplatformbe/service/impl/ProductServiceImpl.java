package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.*;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.*;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.*;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.*;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final IAudienceRepository audienceRepository;
    private final ICategoryRepository categoryRepository;
    private final ISportRepository sportRepository;
    private final IColorRepository colorRepository;
    private final ISizeRepository sizeRepository;
    private final IAttributeValueRepository attributeValueRepository;
    private final IProductVariantRepository productVariantRepository;

    private final EntityManager em;

    // ------------------------------------------------------------------------
    // 1) CREATE PRODUCT  (Node: createProduct)
    // ------------------------------------------------------------------------
    @Override
    public Long createProduct(CreateProductRequest request) {
        Product p = new Product();
        p.setName(request.getName());
        p.setSlug(request.getSlug());
        p.setBasePrice(request.getBasePrice());
        p.setMainImageUrl(request.getMainImageUrl());
        p.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        p.setDescription(request.getDescription());
        p.setSpecifications(request.getSpecifications());
        p.setNote(request.getNote());

        // badge: entity có field Badge, KHÔNG có badgeId riêng
        if (request.getBadgeId() != null) {
            Badge badge = new Badge();
            badge.setId(request.getBadgeId());
            p.setBadge(badge);
        }

        // brand
        if (request.getBrandId() != null) {
            Brand b = new Brand();
            b.setId(request.getBrandId()); // id là Long
            p.setBrand(b);
        }

        productRepository.save(p);
        return p.getId();
    }

    // ------------------------------------------------------------------------
    // 2) GET PRODUCTS BY QUERY (Node: getProductsByQuery)
    // ------------------------------------------------------------------------
    @Override
    public ProductListResultResponse getProductsByQuery(
            String slugCategory,
            String slugAudience,
            String slugBrand,
            String slugSport,
            String q,
            String colorSlugs,   // Node hiện cũng chưa dùng, tạm bỏ qua
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer limit,
            Integer page
    ) {

        int pageSize = (limit != null && limit > 0) ? limit : 16;
        int pageIndex = (page != null && page > 0) ? page : 1;
        int offset = (pageIndex - 1) * pageSize;

        StringBuilder jpql = new StringBuilder(
                "SELECT DISTINCT p FROM Product p " +
                        "LEFT JOIN p.productCategories pc " +
                        "LEFT JOIN pc.category c " +
                        "LEFT JOIN p.productAudiences pa " +
                        "LEFT JOIN pa.audience a " +
                        "LEFT JOIN p.productSports ps " +
                        "LEFT JOIN ps.sport s " +
                        "LEFT JOIN p.brand b "
        );

        StringBuilder countJpql = new StringBuilder(
                "SELECT COUNT(DISTINCT p) FROM Product p " +
                        "LEFT JOIN p.productCategories pc " +
                        "LEFT JOIN pc.category c " +
                        "LEFT JOIN p.productAudiences pa " +
                        "LEFT JOIN pa.audience a " +
                        "LEFT JOIN p.productSports ps " +
                        "LEFT JOIN ps.sport s " +
                        "LEFT JOIN p.brand b "
        );

        List<String> conditions = new ArrayList<>();
        conditions.add("p.isActive = true");

        if (slugCategory != null && !slugCategory.isBlank()) {
            conditions.add("c.slug = :slugCategory");
        }
        if (slugAudience != null && !slugAudience.isBlank()) {
            conditions.add("a.slug = :slugAudience");
        }
        if (slugBrand != null && !slugBrand.isBlank()) {
            conditions.add("b.slug = :slugBrand");
        }
        if (slugSport != null && !slugSport.isBlank()) {
            conditions.add("s.slug = :slugSport");
        }
        if (q != null && !q.isBlank()) {
            conditions.add("LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))");
        }
        if (minPrice != null) {
            conditions.add("p.basePrice >= :minPrice");
        }
        if (maxPrice != null) {
            conditions.add("p.basePrice <= :maxPrice");
        }

        if (!conditions.isEmpty()) {
            String where = " WHERE " + String.join(" AND ", conditions);
            jpql.append(where);
            countJpql.append(where);
        }

        jpql.append(" ORDER BY p.createdAt DESC");

        TypedQuery<Product> query = em.createQuery(jpql.toString(), Product.class);
        TypedQuery<Long> countQuery = em.createQuery(countJpql.toString(), Long.class);

        if (slugCategory != null && !slugCategory.isBlank()) {
            query.setParameter("slugCategory", slugCategory);
            countQuery.setParameter("slugCategory", slugCategory);
        }
        if (slugAudience != null && !slugAudience.isBlank()) {
            query.setParameter("slugAudience", slugAudience);
            countQuery.setParameter("slugAudience", slugAudience);
        }
        if (slugBrand != null && !slugBrand.isBlank()) {
            query.setParameter("slugBrand", slugBrand);
            countQuery.setParameter("slugBrand", slugBrand);
        }
        if (slugSport != null && !slugSport.isBlank()) {
            query.setParameter("slugSport", slugSport);
            countQuery.setParameter("slugSport", slugSport);
        }
        if (q != null && !q.isBlank()) {
            query.setParameter("q", q);
            countQuery.setParameter("q", q);
        }
        if (minPrice != null) {
            query.setParameter("minPrice", minPrice);
            countQuery.setParameter("minPrice", minPrice);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
            countQuery.setParameter("maxPrice", maxPrice);
        }

        query.setFirstResult(offset);
        query.setMaxResults(pageSize);

        List<Product> products = query.getResultList();
        long total = countQuery.getSingleResult();

        // Transform -> ProductListItemResponse giống Node
        List<ProductListItemResponse> list = products.stream().map(p -> {
            ProductListItemResponse dto = new ProductListItemResponse();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setSlug(p.getSlug());
            dto.setMainImageUrl(p.getMainImageUrl());
            dto.setBasePrice(p.getBasePrice());
            dto.setBadgeId(p.getBadge() != null ? p.getBadge().getId() : null);
            dto.setBrandName(p.getBrand() != null ? p.getBrand().getName() : null);

            // colors: unique hexCode from variants
            Set<String> colorSet = new LinkedHashSet<>();
            if (p.getVariants() != null) {
                for (ProductVariant v : p.getVariants()) {
                    if (v.getColor() != null && v.getColor().getHexCode() != null) {
                        colorSet.add(v.getColor().getHexCode());
                    }
                }
            }
            dto.setColors(new ArrayList<>(colorSet));

            // sizes: unique size name from variants
            Set<String> sizeSet = new LinkedHashSet<>();
            if (p.getVariants() != null) {
                for (ProductVariant v : p.getVariants()) {
                    if (v.getSize() != null && v.getSize().getName() != null) {
                        sizeSet.add(v.getSize().getName());
                    }
                }
            }
            dto.setSizes(new ArrayList<>(sizeSet));

            return dto;
        }).collect(Collectors.toList());

        ProductListResultResponse result = new ProductListResultResponse();
        result.setProducts(list);

        PaginationResponse pagination = new PaginationResponse();
        pagination.setTotal(total);
        pagination.setPage(pageIndex);
        pagination.setLimit(pageSize);
        int totalPages = (int) Math.ceil((double) total / pageSize);
        pagination.setTotalPages(totalPages);

        result.setPagination(pagination);

        return result;
    }

    // ------------------------------------------------------------------------
    // 3) Add Audience to Product (Node: addAudienceToProduct)
    // ------------------------------------------------------------------------
    @Override
    public void addAudienceToProduct(Long productId, Long audienceId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Audience audience = audienceRepository.findById(audienceId)
                .orElseThrow(() -> new RuntimeException("Audience not found"));

        ProductAudience pa = new ProductAudience();
        pa.setProduct(product);
        pa.setAudience(audience);

        if (product.getProductAudiences() == null) {
            product.setProductAudiences(new ArrayList<>());
        }
        product.getProductAudiences().add(pa);
        productRepository.save(product);
    }

    // ------------------------------------------------------------------------
    // 4) Add Category to Product (Node: addCategoryToProduct)
    // ------------------------------------------------------------------------
    @Override
    public void addCategoryToProduct(Long productId, Long categoryId, Boolean isPrimary) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        ProductCategory pc = new ProductCategory();
        pc.setProduct(product);
        pc.setCategory(category);
        pc.setIsPrimary(Boolean.TRUE.equals(isPrimary));

        if (product.getProductCategories() == null) {
            product.setProductCategories(new ArrayList<>());
        }
        product.getProductCategories().add(pc);
        productRepository.save(product);
    }

    // ------------------------------------------------------------------------
    // 5) Add Sport to Product (Node: addSportToProduct)
    // ------------------------------------------------------------------------
    @Override
    public void addSportToProduct(Long productId, Long sportId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new RuntimeException("Sport not found"));

        ProductSport ps = new ProductSport();
        ps.setProduct(product);
        ps.setSport(sport);

        if (product.getProductSports() == null) {
            product.setProductSports(new ArrayList<>());
        }
        product.getProductSports().add(ps);
        productRepository.save(product);
    }

    // ------------------------------------------------------------------------
    // 6) Add Variant to Product (Node: addVariantToProduct)
    // ------------------------------------------------------------------------
    @Override
    public ProductVariant addVariantToProduct(Long productId, ProductVariantRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Color color = colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new RuntimeException("Color not found"));

        Size size = sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new RuntimeException("Size not found"));

        // Check existing variant (productId, colorId, sizeId)
        List<ProductVariant> existing = productVariantRepository.findByProductId(productId);
        boolean exists = existing.stream().anyMatch(v ->
                v.getColor().getId().equals(color.getId()) &&
                        v.getSize().getId().equals(size.getId())
        );
        if (exists) {
            throw new RuntimeException("Variant already exists");
        }

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setColor(color);
        variant.setSize(size);
        variant.setPrice(request.getPrice());
        variant.setStockQuantity(
                request.getStockQuantity() != null ? request.getStockQuantity() : 0
        );

        String sku = request.getSku();
        if (sku == null || sku.isBlank()) {
            sku = "SKU-" + productId + "-" + product.getSlug() + "-" +
                    color.getId() + "-" + size.getId();
        }
        variant.setSku(sku);

        return productVariantRepository.save(variant);
    }

    // ------------------------------------------------------------------------
    // 7) Delete Variant from Product (Node: deleteVariantFromProduct)
    // ------------------------------------------------------------------------
    @Override
    public void deleteVariantFromProduct(Long productId, Long variantId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        // detach from product list (nếu dùng mappedBy ở Product)
        if (product.getVariants() != null) {
            product.getVariants().removeIf(v -> v.getId().equals(variantId));
        }

        productVariantRepository.delete(variant);
    }

    // ------------------------------------------------------------------------
    // 8) Get Variants by Product (Node: getVariantsByProductId)
    // ------------------------------------------------------------------------
    private ProductVariantAdminResponse toVariantAdminResponse(ProductVariant v) {
        ProductVariantAdminResponse dto = new ProductVariantAdminResponse();
        dto.setId(v.getId());
        dto.setProductId(v.getProduct().getId());
        dto.setColorId(v.getColor() != null ? v.getColor().getId() : null);
        dto.setSizeId(v.getSize() != null ? v.getSize().getId() : null);
        dto.setPrice(v.getPrice());
        dto.setStockQuantity(v.getStockQuantity());
        dto.setSku(v.getSku());

        // nếu imageUrls đang là String JSON, bạn có thể để Object hoặc parse ra List<String> tùy bạn
        // giả sử tạm thời bạn lưu List<String> trong entity:
        // imageUrls bây giờ là List<String>
        if (v.getImageUrls() != null && !v.getImageUrls().isEmpty()) {
            dto.setImageUrls(new ArrayList<>(v.getImageUrls())); // copy ra list riêng cho DTO
        } else {
            dto.setImageUrls(Collections.emptyList());
        }


        // product summary
        Product product = v.getProduct();
        ProductVariantAdminResponse.ProductSummary ps =
                new ProductVariantAdminResponse.ProductSummary();
        ps.setId(product.getId());
        ps.setName(product.getName());
        ps.setSlug(product.getSlug());
        ps.setBrandId(product.getBrand() != null ? product.getBrand().getId() : null);
        ps.setBasePrice(product.getBasePrice());
        ps.setMainImageUrl(product.getMainImageUrl());
        ps.setIsActive(product.getIsActive());
        ps.setDescription(product.getDescription());
        ps.setSpecifications(product.getSpecifications());
        ps.setNote(product.getNote());
        ps.setBadgeId(product.getBadge() != null ? product.getBadge().getId() : null);
        ps.setCreatedAt(product.getCreatedAt());
        ps.setUpdatedAt(product.getUpdatedAt());
        dto.setProduct(ps);

        return dto;
    }

    @Override
    public List<ProductVariantAdminResponse> getVariantsByProductId(Long productId) {
        List<ProductVariant> variants = productVariantRepository.findByProductId(productId);
        List<ProductVariantAdminResponse> result = new ArrayList<>();
        for (ProductVariant v : variants) {
            result.add(toVariantAdminResponse(v));
        }
        return result;
    }

    // ------------------------------------------------------------------------
    // 9) Update Variant of Product (Node: updateVariantOfProduct)
    // ------------------------------------------------------------------------
    @Override
    public ProductVariant updateVariantOfProduct(Long productId, Long variantId, UpdateProductVariantRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        if (request.getColorId() != null) {
            Color color = colorRepository.findById(request.getColorId())
                    .orElseThrow(() -> new RuntimeException("Color not found"));
            variant.setColor(color);
        }
        if (request.getSizeId() != null) {
            Size size = sizeRepository.findById(request.getSizeId())
                    .orElseThrow(() -> new RuntimeException("Size not found"));
            variant.setSize(size);
        }
        if (request.getPrice() != null) {
            variant.setPrice(request.getPrice());
        }
        if (request.getStockQuantity() != null) {
            variant.setStockQuantity(request.getStockQuantity());
        }
        if (request.getSku() != null) {
            variant.setSku(request.getSku());
        }
        if (request.getImageUrls() != null) {
            variant.setImageUrls(new ArrayList<>(request.getImageUrls()));
        }


        return productVariantRepository.save(variant);
    }

    // ------------------------------------------------------------------------
    // 10) Add AttributeValue to Product (Node: addAttributeValueToProduct)
    // ------------------------------------------------------------------------
    @Override
    public void addAttributeValueToProduct(Long productId, Long attributeValueId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        AttributeValue attrValue = attributeValueRepository.findById(attributeValueId)
                .orElseThrow(() -> new RuntimeException("Attribute Value not found"));

        ProductAttributeValue pav = new ProductAttributeValue();
        pav.setProduct(product);
        pav.setAttributeValue(attrValue);

        if (product.getProductAttributeValues() == null) {
            product.setProductAttributeValues(new ArrayList<>());
        }
        product.getProductAttributeValues().add(pav);

        productRepository.save(product);
    }

    // ------------------------------------------------------------------------
    // 11) getAudiencesByProductId / getCategoriesByProductId (Node)
    // ------------------------------------------------------------------------
    @Override
    public List<ProductDetailResponse.AudienceSummaryResponse> getAudiencesByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElse(null);

        // Không tìm thấy product -> trả list rỗng
        if (product == null) {
            return Collections.emptyList();
        }

        return product.getProductAudiences().stream()
                .map(pa -> {
                    Audience a = pa.getAudience();
                    ProductDetailResponse.AudienceSummaryResponse dto =
                            new ProductDetailResponse.AudienceSummaryResponse();
                    dto.setId(a.getId());
                    dto.setName(a.getName());
                    dto.setSlug(a.getSlug());
                    dto.setSortOrder(a.getSortOrder());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDetailResponse.CategorySummaryResponse> getCategoriesByProductId(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return Collections.emptyList();
        }

        return product.getProductCategories().stream()
                .map(pc -> {
                    Category c = pc.getCategory();
                    ProductDetailResponse.CategorySummaryResponse dto =
                            new ProductDetailResponse.CategorySummaryResponse();

                    dto.setId(c.getId());
                    dto.setParentId(c.getParent() != null ? c.getParent().getId() : null);
                    dto.setName(c.getName());
                    dto.setSlug(c.getSlug());
                    dto.setIsActive(c.getIsActive());
                    dto.setCreatedAt(c.getCreatedAt());
                    dto.setUpdatedAt(c.getUpdatedAt());

                    return dto;
                })
                .collect(Collectors.toList());
    }


    // ------------------------------------------------------------------------
    // 12) getProductBySlug (Node: getProductBySlug)
    // ------------------------------------------------------------------------
    @Override
    public ProductDetailResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElse(null);

        // Không tìm thấy -> trả null, dừng luôn, không đụng vào product.*
        if (product == null) {
            return null;
        }

        ProductDetailResponse dto = new ProductDetailResponse();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setBasePrice(product.getBasePrice());
        dto.setDescription(product.getDescription());
        dto.setSpecifications(product.getSpecifications());
        dto.setNote(product.getNote());

        if (product.getBrand() != null) {
            ProductDetailResponse.BrandSummary brandSummary =
                    new ProductDetailResponse.BrandSummary();
            brandSummary.setId(product.getBrand().getId());
            brandSummary.setName(product.getBrand().getName());
            brandSummary.setSlug(product.getBrand().getSlug());
            dto.setBrand(brandSummary);
            dto.setBrandName(product.getBrand().getName());
        }

        // 1. Colors (unique by colorId)
        Map<Long, ProductDetailResponse.ColorResponse> colorMap = new LinkedHashMap<>();
        if (product.getVariants() != null) {
            for (ProductVariant v : product.getVariants()) {
                Color c = v.getColor();
                if (c != null && !colorMap.containsKey(c.getId())) {
                    ProductDetailResponse.ColorResponse cDto =
                            new ProductDetailResponse.ColorResponse();
                    cDto.setId(c.getId());
                    cDto.setName(c.getName());
                    cDto.setHexCode(c.getHexCode());
                    colorMap.put(c.getId(), cDto);
                }
            }
        }
        dto.setColors(new ArrayList<>(colorMap.values()));

        // 2. Sizes (unique size name)
        Set<String> sizeSet = new LinkedHashSet<>();
        if (product.getVariants() != null) {
            for (ProductVariant v : product.getVariants()) {
                if (v.getSize() != null && v.getSize().getName() != null) {
                    sizeSet.add(v.getSize().getName());
                }
            }
        }
        dto.setSizes(new ArrayList<>(sizeSet));

        // 3. Attributes (group by attribute name => list of values)
        Map<String, List<String>> attrMap = new LinkedHashMap<>();
        if (product.getProductAttributeValues() != null) {
            for (ProductAttributeValue pav : product.getProductAttributeValues()) {
                AttributeValue value = pav.getAttributeValue();
                if (value != null && value.getAttribute() != null) {
                    String name = value.getAttribute().getName();
                    String val = value.getValue();
                    attrMap.putIfAbsent(name, new ArrayList<>());
                    List<String> list = attrMap.get(name);
                    if (!list.contains(val)) {
                        list.add(val);
                    }
                }
            }
        }
        List<ProductDetailResponse.ProductAttributeGroupResponse> attrDtos =
                attrMap.entrySet().stream().map(e -> {
                    ProductDetailResponse.ProductAttributeGroupResponse g =
                            new ProductDetailResponse.ProductAttributeGroupResponse();
                    g.setName(e.getKey());
                    g.setValue(e.getValue());
                    return g;
                }).collect(Collectors.toList());
        dto.setAttributes(attrDtos);

        // 4. Variants
        List<ProductDetailResponse.ProductVariantResponse> variantDtos = new ArrayList<>();
        if (product.getVariants() != null) {
            for (ProductVariant v : product.getVariants()) {
                ProductDetailResponse.ProductVariantResponse vDto =
                        new ProductDetailResponse.ProductVariantResponse();
                vDto.setId(v.getId());
                vDto.setSku(v.getSku());
                vDto.setPrice(
                        v.getPrice() != null ? v.getPrice() : product.getBasePrice()
                );
                vDto.setStockQuantity(v.getStockQuantity());
                vDto.setColorId(v.getColor() != null ? v.getColor().getId() : null);
                vDto.setSizeName(v.getSize() != null ? v.getSize().getName() : null);
                if (v.getImageUrls() != null) {
                    vDto.setImageUrls(new ArrayList<>(v.getImageUrls()));
                } else {
                    vDto.setImageUrls(Collections.emptyList());
                }
                // String
                variantDtos.add(vDto);
            }
        }
        dto.setVariants(variantDtos);

        // categories
        List<ProductDetailResponse.CategorySummaryResponse> catDtos =
                product.getProductCategories().stream().map(pc -> {
                    Category c = pc.getCategory();
                    ProductDetailResponse.CategorySummaryResponse cDto =
                            new ProductDetailResponse.CategorySummaryResponse();
                    cDto.setId(c.getId());
                    cDto.setName(c.getName());
                    cDto.setSlug(c.getSlug());
                    return cDto;
                }).collect(Collectors.toList());
        dto.setCategories(catDtos);

        // audiences
        List<ProductDetailResponse.AudienceSummaryResponse> audDtos =
                product.getProductAudiences().stream().map(pa -> {
                    Audience a = pa.getAudience();
                    ProductDetailResponse.AudienceSummaryResponse aDto =
                            new ProductDetailResponse.AudienceSummaryResponse();
                    aDto.setId(a.getId());
                    aDto.setName(a.getName());
                    aDto.setSlug(a.getSlug());
                    return aDto;
                }).collect(Collectors.toList());
        dto.setAudiences(audDtos);

        // sports
        List<ProductDetailResponse.SportSummaryResponse> sportDtos =
                product.getProductSports().stream().map(ps -> {
                    Sport s = ps.getSport();
                    ProductDetailResponse.SportSummaryResponse sDto =
                            new ProductDetailResponse.SportSummaryResponse();
                    sDto.setId(s.getId());
                    sDto.setName(s.getName());
                    sDto.setSlug(s.getSlug());
                    return sDto;
                }).collect(Collectors.toList());
        dto.setSports(sportDtos);

        return dto;
    }
}
