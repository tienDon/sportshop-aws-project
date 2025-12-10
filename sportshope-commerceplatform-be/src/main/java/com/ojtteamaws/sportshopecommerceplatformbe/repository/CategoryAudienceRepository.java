package com.ojtteamaws.sportshopecommerceplatformbe.repository;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.Category;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.CategoryAudience;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Audience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryAudienceRepository extends JpaRepository<CategoryAudience, Long> {

    boolean existsByCategoryAndAudience(Category category, Audience audience);
}
