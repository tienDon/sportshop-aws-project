package com.ojtteamaws.sportshopecommerceplatformbe.controller;


import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.AudienceDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Audience;
import com.ojtteamaws.sportshopecommerceplatformbe.service.impl.AudienceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audiences")
public class AudienceController {

    private final AudienceService audienceService;

    public AudienceController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAudiences() {
        List<AudienceDTO> audiences = audienceService.getAllAudiences(); // ✅ trả DTO từ service

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Audiences retrieved successfully");
        response.put("data", audiences);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAudienceById(@PathVariable Long id) {
        AudienceDTO audience = audienceService.getAudienceById(id); // ✅ trả DTO từ service

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Audience retrieved successfully");
        response.put("data", audience);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createAudience(@RequestBody Audience audienceRequest) {
        Audience newAudience = audienceService.createAudience(audienceRequest.getName(), audienceRequest.getSlug() , audienceRequest.getSortOrder());
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Audience created successfully");
        response.put("data", newAudience);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAudience(@PathVariable Long id, @RequestBody Audience audienceRequest) {
        Audience updatedAudience = audienceService.updateAudience(id, audienceRequest.getName(), audienceRequest.getSlug() , audienceRequest.getSortOrder());
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Audience updated successfully");
        response.put("data", updatedAudience);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAudience(@PathVariable Long id) {
        audienceService.deleteAudience(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Audience deleted successfully");
        return ResponseEntity.ok(response);
    }
}
