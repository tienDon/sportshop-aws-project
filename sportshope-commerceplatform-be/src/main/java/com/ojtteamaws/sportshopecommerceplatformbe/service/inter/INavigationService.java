package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.NavigationSectionResponse;

import java.util.List;

public interface INavigationService {

    List<NavigationSectionResponse> getMainNavigation();
}
