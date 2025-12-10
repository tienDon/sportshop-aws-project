package com.ojtteamaws.sportshopecommerceplatformbe.repository;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameAndParentIsNull(String name);

    boolean existsBySlugAndParentIsNull(String slug);
    Optional<Category> findBySlug(String slug);


    List<Category> findByParentIsNull();

    @Query("""
           select distinct c from Category c
           join c.categoryAudiences ca
           join ca.audience a
           where c.parent is null and a.slug = :slug
           """)
    List<Category> findRootByAudienceSlug(@Param("slug") String slug);
}
