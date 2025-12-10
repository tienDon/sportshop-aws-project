package com.ojtteamaws.sportshopecommerceplatformbe.repository;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.Audience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudienceRepository extends JpaRepository<Audience, Long> {
    Optional<Audience> findByNameOrSlug(String name, String slug);

    Audience findAudienceById(Long id);
    Optional<Audience> findBySlug(String slug);
}