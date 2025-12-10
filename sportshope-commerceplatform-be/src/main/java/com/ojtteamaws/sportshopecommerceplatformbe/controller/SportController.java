package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.SportDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Color;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Sport;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.SportRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.service.impl.SportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sports")

public class SportController {

    @Autowired
    private SportService sportService;

    @GetMapping()
    public ResponseEntity<List<SportDTO>> getAllSport() {
        return ResponseEntity.ok(sportService.getAllSports());
    }
    @PostMapping()
    public ResponseEntity<?> createSport(@Valid @RequestBody Sport request) {
        try {
            Sport sport = sportService.createSport(request);
            return ResponseEntity.ok(sport);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSport(
            @PathVariable Long id,
            @Valid @RequestBody Sport request
    ) {
        try {

            Sport sport = sportService.updateSport( request , id);
            return ResponseEntity.ok(sport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSport(@PathVariable Long id) {
        try {
            sportService.deleteSportById(id);
            return ResponseEntity.ok("Sport deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
