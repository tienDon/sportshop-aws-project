package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.SizeDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Size;
import com.ojtteamaws.sportshopecommerceplatformbe.service.impl.SizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/size")
public class SizeController {

  @Autowired
  SizeService sizeService;

    @GetMapping
    public ResponseEntity<?> getAllSizes(@RequestParam(required = false) String chartType) {
        try {
            if (chartType != null) {
                return ResponseEntity.ok(sizeService.getSizeByChartType(chartType));
            }
            return ResponseEntity.ok(sizeService.getAllSizes());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching sizes: " + e.getMessage());
        }
    }

    @GetMapping("/{chartType}")
    public ResponseEntity<?> getSizeByChartSize(@PathVariable String chartType) {
        try {

            List<SizeDTO> sizes = sizeService.getSizeByChartType(chartType);
            return ResponseEntity.ok(sizes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching sizes: " + e.getMessage());
        }
    }


    @PostMapping()
    public ResponseEntity<?> createSize(@RequestBody Size request) {

if(request == null) {
    return ResponseEntity.status(500).body("Error creating new size , missing data !");
}
        Size size = sizeService.createSize(request);

        return ResponseEntity.ok(
                Map.of(
                        "data", size,

                        "message", "New size created successfully"
                )
        );

    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSize(
            @PathVariable Long id,
            @Valid @RequestBody Size request
    ) {
        try {
            Size size = sizeService.updateSize(request, id);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Brand updated successfully",
                            "data", size
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
    public ResponseEntity<?> deleteSize(@PathVariable Long id) {
        try {
            sizeService.deleteSizeById(id);

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

