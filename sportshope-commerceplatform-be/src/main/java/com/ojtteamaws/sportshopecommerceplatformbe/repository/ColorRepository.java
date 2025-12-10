package com.ojtteamaws.sportshopecommerceplatformbe.repository;


import com.ojtteamaws.sportshopecommerceplatformbe.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {

    List<Color> findAllByOrderByIdAsc() ;

    boolean existsByName(String name);

    Color getColorById(long id);
}
