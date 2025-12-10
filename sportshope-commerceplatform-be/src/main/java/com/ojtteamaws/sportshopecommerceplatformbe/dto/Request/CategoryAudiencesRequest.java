package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryAudiencesRequest {

    private List<Long> audienceIds;
    private Integer sortOrder; // có thể null -> mặc định 0
}
