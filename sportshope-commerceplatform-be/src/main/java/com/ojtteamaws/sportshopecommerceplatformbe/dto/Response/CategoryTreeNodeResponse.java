package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryTreeNodeResponse {

    private Long id;
    private String name;
    private String slug;
    private List<CategoryTreeNodeResponse> children;
}
