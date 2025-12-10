package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.BrandDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Color;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.BrandRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandDTO> getAllBrands() {
        return brandRepository.findBrandByIsActiveTrue()
                .stream()
                .map(b -> new BrandDTO(
                        b.getId(),
                        b.getName(),
                        b.getSlug(),
                        b.getLogo(),
                        b.getDescription(),
                        b.getBanner(),
                        b.getIsActive()
                ))
                .toList();
    }

    public Brand createBrand(Brand request) {
        // Kiểm tra slug đã tồn tại chưa
        if (brandRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug brand đã tồn tại!");
        }

        Brand brand = Brand.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .logo(request.getLogo())
                .banner(request.getBanner())
                .description(request.getDescription())
                .isActive(request.getIsActive())
                .build();

        return brandRepository.save(brand);
    }


    public Brand updateBrand(@Valid Brand request , long id) {
        Brand brand = brandRepository.findBrandById(id);
        if(request.getName() != null) { brand.setName(request.getName()); }
        if (request.getLogo() != null) { brand.setLogo(request.getLogo()); }
        if(request.getBanner() != null) { brand.setBanner(request.getBanner()); }
        if(request.getDescription() != null) { brand.setDescription(request.getDescription()); }
        if(request.getSlug() != null) { brand.setSlug(request.getSlug()); }

        return brandRepository.save(brand);
    }

    public void deleteBrandById(Long id) {
        Brand brand = brandRepository.findBrandById(id);
        brand.setIsActive(false);
        brandRepository.save(brand);
    }
}
