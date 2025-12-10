package com.ojtteamaws.sportshopecommerceplatformbe.repository;


import com.ojtteamaws.sportshopecommerceplatformbe.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
    List<AttributeValue> findByAttributeId(Long attributeId);
}
