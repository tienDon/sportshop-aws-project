package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;


import com.ojtteamaws.sportshopecommerceplatformbe.entity.Size;
import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.SizeChartType;
import lombok.Data;

@Data
public class SizeDTO {
    private Long id;
    private String name;
    private String chartType;
    private Integer sortOrder;
    private Boolean isActive;

    public SizeDTO(Size size) {
        this.id = size.getId();
        this.name = size.getName();
        this.chartType = String.valueOf(size.getChartType());
        this.sortOrder = size.getSortOrder();
        this.isActive = size.getIsActive();
    }

    public SizeDTO(Long id, String name, Integer sortOrder, SizeChartType chartType, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.sortOrder = sortOrder;
        this.chartType = chartType.name();
        this.isActive = isActive;
    }
}
