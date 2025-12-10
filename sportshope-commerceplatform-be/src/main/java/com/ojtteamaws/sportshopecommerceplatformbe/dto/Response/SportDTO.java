package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportDTO {
    private Long id;
    private String name;
    private String slug;
    private Boolean isActive;
    private Integer sortOrder;
}
