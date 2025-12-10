package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;


import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) return null;

        User userDetails = (User) auth.getPrincipal();
        return userDetails.getId(); // id trong JWT
    }
}
