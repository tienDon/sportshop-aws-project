package com.ojtteamaws.sportshopecommerceplatformbe.controller;



import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.AttributeBasicDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.AttributeDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.AttributeValueDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.AttributeWithValuesDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Attribute;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.AttributeValue;
import com.ojtteamaws.sportshopecommerceplatformbe.service.impl.AttributeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController {

    private final AttributeService attributeService;

    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAttributes() {
        List<AttributeBasicDTO> attributes = attributeService.getAllAttributes().stream()
                .map(attr -> new AttributeBasicDTO(
                        attr.getId(),
                        attr.getName(),
                        attr.getCode(),
                        attr.getCreatedAt(),
                        attr.getUpdatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Fetched all attributes successfully",
                "data", attributes
        ));
    }


    @PostMapping
    public ResponseEntity<?> createAttribute(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String code = body.get("code");
        if (name == null || code == null || name.trim().isEmpty() || code.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Name and code are required"
            ));
        }
        Attribute attribute = attributeService.createAttribute(name, code);
        return ResponseEntity.status(201).body(Map.of(
                "success", true,
                "message", "Attribute created successfully"
        ));
    }

    @PostMapping("/{attributeId}/values")
    public ResponseEntity<?> createAttributeValue(
            @PathVariable Long attributeId,
            @RequestBody Map<String, String> body
    ) {
        String value = body.get("value");
        Integer sortOrder = null;
        if (body.get("sortOrder") != null) {
            sortOrder = Integer.parseInt(body.get("sortOrder").toString());
        }        AttributeValue attributeValue = attributeService.createAttributeValue(attributeId, value , sortOrder) ;
        return ResponseEntity.status(201).body(Map.of(
                "success", true,
                "message", "Attribute values created successfully"
        ));
    }

    @GetMapping("/{attributeId}/values")
    public ResponseEntity<?> getAttributeValuesByAttributeId(@PathVariable Long attributeId) {
        List<AttributeValueDTO> values = attributeService.getAttributeValuesByAttributeId(attributeId)
                .stream()
                .map(v -> new AttributeValueDTO(
                        v.getId(),
                        v.getAttribute().getId(),  // attributeId
                        v.getValue()    ,
                        v.getSortOrder()// value
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "attributeId", attributeId,
                "success", true,
                "message", "Fetched attribute values successfully",
                "data", values
        ));
    }


    @GetMapping("/with-values")
    public ResponseEntity<?> getAttributesWithValues(@RequestParam(required = false) List<String> code) {
        List<AttributeWithValuesDTO> attributes = attributeService.getAttributesWithValues(code)
                .stream()
                .map(attr -> {

                    List<AttributeValueDTO> values = attr.getValues().stream()
                            .map(v -> new AttributeValueDTO(
                                    v.getId(),
                                    v.getAttribute().getId(),
                                    v.getValue(),
                                    v.getSortOrder()
                            ))
                            .collect(Collectors.toList());

                    return new AttributeWithValuesDTO(
                            attr.getId(),
                            attr.getName(),
                            attr.getCode(),
                            values
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Fetched attributes with values successfully",
                "data", attributes
        ));
    }

    // DELETE /api/attributes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttribute(@PathVariable Long id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.ok().body("Attribute deleted successfully");
    }

    // PATCH /api/attributes/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAttribute(
            @PathVariable Long id,
            @RequestBody Attribute request
    ) {
        Attribute updated = attributeService.updateAttribute(id, request.getName(), request.getCode());
        return ResponseEntity.ok("Attribute updated successfully");
    }

    // PATCH /api/attributes/values/{id}
    @PatchMapping("/values/{id}")
    public ResponseEntity<?> updateAttributeValue(
            @PathVariable Long id,
            @RequestBody AttributeValue request
    ) {
        AttributeValue updated = attributeService.updateAttributeValue(id, request.getValue(), request.getSortOrder());
        return ResponseEntity.ok("Attribute value updated successfully");
    }

    // DELETE /api/attributes/values/{id}
    @DeleteMapping("/values/{id}")
    public ResponseEntity<?> deleteAttributeValue(@PathVariable Long id) {
        attributeService.deleteAttributeValue(id);
        return ResponseEntity.ok().body("Attribute value deleted successfully");
    }



}
