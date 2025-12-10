package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductAttributeGroupResponse {
    private String name;        // attribute name
    private List<String> value; // list value (Cotton, Polyester,...)
}
