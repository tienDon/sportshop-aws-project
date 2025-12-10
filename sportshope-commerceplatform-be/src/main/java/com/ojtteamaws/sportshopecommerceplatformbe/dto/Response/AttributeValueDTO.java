package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueDTO {
    private Long id;
    private long attributeId;
    private String value;
    private Integer sortOrder;
}
