package com.ojtteamaws.sportshopecommerceplatformbe.repository;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISportRepository extends JpaRepository<Sport, Long> {
}
