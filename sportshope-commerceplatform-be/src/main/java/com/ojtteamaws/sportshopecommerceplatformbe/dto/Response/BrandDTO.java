package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDTO {

    private Long id;
    private String name;
    private String slug;
    private String logo;
    private String description;
    private String banner;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BrandDTO(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.slug = brand.getSlug();
        this.logo = brand.getLogo();
        this.description = brand.getDescription();
        this.banner = brand.getBanner();
        this.isActive = brand.getIsActive();
        this.createdAt = brand.getCreatedAt();
        this.updatedAt = brand.getUpdatedAt();
    }
    public BrandDTO(Long id, String name, String slug, String logo,
                    String description, String banner, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.logo = logo;
        this.description = description;
        this.banner = banner;
        this.isActive = isActive;
    }



}
