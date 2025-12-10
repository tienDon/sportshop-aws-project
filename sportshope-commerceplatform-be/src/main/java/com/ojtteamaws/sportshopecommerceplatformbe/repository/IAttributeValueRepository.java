package com.ojtteamaws.sportshopecommerceplatformbe.repository;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAttributeValueRepository extends JpaRepository<AttributeValue, Long> {
    // Không cần viết gì thêm, JpaRepository đã có sẵn findById, save, delete,...
}
