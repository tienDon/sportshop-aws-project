package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationResponse {
    private long total;
    private int page;
    private int limit;
    private int totalPages;
}
