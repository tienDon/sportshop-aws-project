package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.NavigationSectionResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.INavigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/navigation")
@RequiredArgsConstructor
public class NavigationController {

    private final INavigationService navigationService;

    @GetMapping("/main")
    public ResponseEntity<?> getMainNavigation() {
        List<NavigationSectionResponse> navigation = navigationService.getMainNavigation();
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "data", navigation
                )
        );
    }
}
