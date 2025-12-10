package com.ojtteamaws.sportshopecommerceplatformbe.repository;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCart_IdAndVariant_Id(Long cartId, Long variantId);

    Optional<CartItem> findByIdAndCart_Id(Long id, Long cartId);

    Long countByCart_Id(Long cartId);

    void deleteByCart_Id(Long cartId);

    void deleteByCartUserId(Long userId);
}
