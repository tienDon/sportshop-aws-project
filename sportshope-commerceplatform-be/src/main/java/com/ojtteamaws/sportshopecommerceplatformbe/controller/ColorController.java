package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ColorDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Color;
import com.ojtteamaws.sportshopecommerceplatformbe.service.impl.BrandService;
import com.ojtteamaws.sportshopecommerceplatformbe.service.impl.ColorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
public class ColorController {

    @Autowired
    ColorService colorService;
    @GetMapping("")
    public ResponseEntity<List<ColorDTO>> getAllColors() {
        return ResponseEntity.ok(colorService.getAllColors());
    }

    @PostMapping
    public ResponseEntity<?> createColor(@Valid @RequestBody Color request) {
        try {
            Color color = colorService.createColor(request);
            return ResponseEntity.ok(color);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateColor(
            @PathVariable Long id,
            @Valid @RequestBody Color request
    ) {
        try {
            Color color = colorService.updateColor( request , id);
            return ResponseEntity.ok(color);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteColor(@PathVariable Long id) {
        try {
            colorService.deleteColorById(id);
            return ResponseEntity.ok("Color deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
