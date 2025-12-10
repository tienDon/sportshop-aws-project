package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class AttributeDTO {
    private Long id;
    private String name;
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AttributeValueDTO> values;


    public AttributeDTO(Long id, String name, String code, LocalDateTime createdAt, LocalDateTime updatedAt, List<AttributeValueDTO> values) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.values=values;

    }
    //private List<AttributeValueDTO> values;
}

