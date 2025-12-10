package com.ojtteamaws.sportshopecommerceplatformbe.repository;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}