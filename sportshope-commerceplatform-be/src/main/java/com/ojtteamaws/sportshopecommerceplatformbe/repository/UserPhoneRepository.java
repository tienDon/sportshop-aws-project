package com.ojtteamaws.sportshopecommerceplatformbe.repository;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.UserPhone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPhoneRepository extends JpaRepository<UserPhone, Long> {

    List<UserPhone> findByUser_Id(Long userId);
}
