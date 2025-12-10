package com.ojtteamaws.sportshopecommerceplatformbe.controller;


import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.BrandDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Color;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.BrandRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.service.impl.BrandService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/brands")


public class BrandController {

    @Autowired
    BrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@Valid @RequestBody Brand request) {
        try {
            Brand brand = brandService.createBrand(request);
            return ResponseEntity.ok(brand);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody Brand request
    ) {
        try {
            Brand brand = brandService.updateBrand(request, id);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Brand updated successfully",
                            "data", brand
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "error", e.getMessage()
                    )
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Long id) {
        try {
            brandService.deleteBrandById(id);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Brand deleted successfully"
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "error", e.getMessage()
                    )
            );
        }
    }



}
