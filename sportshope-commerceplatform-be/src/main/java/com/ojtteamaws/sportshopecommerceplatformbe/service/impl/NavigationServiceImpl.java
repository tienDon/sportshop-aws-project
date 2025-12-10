package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.CategoryTreeNodeResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.NavigationItemResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.NavigationSectionResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Sport;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.BrandRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.SportRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.ICategoryService;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.INavigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NavigationServiceImpl implements INavigationService {

    private final ICategoryService categoryService;
    private final BrandRepository brandRepository;
    private final SportRepository sportRepository;

    @Override
    public List<NavigationSectionResponse> getMainNavigation() {
        // 1. Lấy dữ liệu song song (ở Spring thì cứ gọi lần lượt, DB vẫn ok)
        List<CategoryTreeNodeResponse> menCategories =
                categoryService.getCategoryByAudienceSlug("nam");
        List<CategoryTreeNodeResponse> womenCategories =
                categoryService.getCategoryByAudienceSlug("nu");
        List<CategoryTreeNodeResponse> kidsCategories =
                categoryService.getCategoryByAudienceSlug("tre-em");

        // ID 22 là "Phụ Kiện" theo Prisma cũ
        CategoryTreeNodeResponse accessoriesCategory =
                categoryService.getCategoryById(22L);

        List<Brand> brands = brandRepository.findByIsActiveTrueOrderByNameAsc();
        List<Sport> sports = sportRepository.findByIsActiveTrueOrderBySortOrderAscNameAsc();

        // Helper: map CategoryTreeNodeResponse -> NavigationItemResponse (đệ quy)
        List<NavigationItemResponse> menChildren = new ArrayList<>();
        for (CategoryTreeNodeResponse c : menCategories) {
            menChildren.add(mapCategoryToNav(c));
        }

        List<NavigationItemResponse> womenChildren = new ArrayList<>();
        for (CategoryTreeNodeResponse c : womenCategories) {
            womenChildren.add(mapCategoryToNav(c));
        }

        List<NavigationItemResponse> kidsChildren = new ArrayList<>();
        for (CategoryTreeNodeResponse c : kidsCategories) {
            kidsChildren.add(mapCategoryToNav(c));
        }

        List<NavigationItemResponse> accessoriesChildren = new ArrayList<>();
        if (accessoriesCategory != null && accessoriesCategory.getChildren() != null) {
            for (CategoryTreeNodeResponse c : accessoriesCategory.getChildren()) {
                accessoriesChildren.add(mapCategoryToNav(c));
            }
        }

        // Brand items
        List<NavigationItemResponse> brandItems = new ArrayList<>();
        for (Brand b : brands) {
            NavigationItemResponse item = new NavigationItemResponse();
            item.setId(b.getId());
            item.setName(b.getName());
            item.setSlug(b.getSlug());
            item.setItems(null);
            brandItems.add(item);
        }

        // Column cho brands
        NavigationItemResponse brandsColumn = new NavigationItemResponse();
        brandsColumn.setId(null); // node static
        brandsColumn.setName("CÁC THƯƠNG HIỆU");
        brandsColumn.setSlug(null);
        brandsColumn.setItems(brandItems);

        // Sport items
        List<NavigationItemResponse> sportItems = new ArrayList<>();
        for (Sport s : sports) {
            NavigationItemResponse item = new NavigationItemResponse();
            item.setId(s.getId());
            item.setName(s.getName());
            item.setSlug(s.getSlug());
            item.setItems(null);
            sportItems.add(item);
        }

        NavigationItemResponse sportsColumn = new NavigationItemResponse();
        sportsColumn.setId(null);
        sportsColumn.setName("MÔN THỂ THAO");
        sportsColumn.setSlug(null);
        sportsColumn.setItems(sportItems);

        // 2. Build list NavigationSectionResponse (y chang Node)
        List<NavigationSectionResponse> navigation = new ArrayList<>();

        NavigationSectionResponse menSection = new NavigationSectionResponse();
        menSection.setId("nav-men");
        menSection.setName("Nam");
        menSection.setSlug("nam");
        menSection.setChildren(menChildren);
        navigation.add(menSection);

        NavigationSectionResponse womenSection = new NavigationSectionResponse();
        womenSection.setId("nav-women");
        womenSection.setName("Nữ");
        womenSection.setSlug("nu");
        womenSection.setChildren(womenChildren);
        navigation.add(womenSection);

        NavigationSectionResponse kidsSection = new NavigationSectionResponse();
        kidsSection.setId("nav-kids");
        kidsSection.setName("Trẻ em");
        kidsSection.setSlug("tre-em");
        kidsSection.setChildren(kidsChildren);
        navigation.add(kidsSection);

        NavigationSectionResponse accessoriesSection = new NavigationSectionResponse();
        accessoriesSection.setId("nav-accessories");
        accessoriesSection.setName("Phụ Kiện");
        accessoriesSection.setSlug("phu-kien");
        accessoriesSection.setChildren(accessoriesChildren);
        navigation.add(accessoriesSection);

        NavigationSectionResponse brandSection = new NavigationSectionResponse();
        brandSection.setId("STATIC_BRAND");
        brandSection.setName("Thương Hiệu");
        brandSection.setSlug("thuong-hieu");
        brandSection.setChildren(List.of(brandsColumn));
        navigation.add(brandSection);

        NavigationSectionResponse sportSection = new NavigationSectionResponse();
        sportSection.setId("STATIC_SPORT");
        sportSection.setName("Thể Thao");
        sportSection.setSlug("the-thao");
        sportSection.setChildren(List.of(sportsColumn));
        navigation.add(sportSection);

        return navigation;
    }

    private NavigationItemResponse mapCategoryToNav(CategoryTreeNodeResponse category) {
        NavigationItemResponse dto = new NavigationItemResponse();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());

        List<NavigationItemResponse> childItems = new ArrayList<>();
        if (category.getChildren() != null) {
            for (CategoryTreeNodeResponse child : category.getChildren()) {
                childItems.add(mapCategoryToNav(child));
            }
        }
        dto.setItems(childItems);
        return dto;
    }
}
