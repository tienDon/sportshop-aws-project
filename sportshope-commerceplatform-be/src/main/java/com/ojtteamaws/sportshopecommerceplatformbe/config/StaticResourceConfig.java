package com.ojtteamaws.sportshopecommerceplatformbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Map URL /files/** -> thư mục uploads/
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:uploads/");
    }
}
