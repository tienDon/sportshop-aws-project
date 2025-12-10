package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.SizeDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Size;
import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.SizeChartType;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.ColorRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.SizeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class SizeService {

    @Autowired
    SizeRepository sizeRepository;

    public List<SizeDTO> getAllSizes() {
        List<Size> sizes = sizeRepository.findAllByIsActiveTrue();

        // 🔥 Convert entity → DTO (không dùng mapper)
        return sizes.stream()
                .map(size -> new SizeDTO(
                        size.getId(),
                        size.getName(),
                        size.getSortOrder(),
                        size.getChartType(),
                        size.getIsActive()
                ))
                .toList();
    }

    public List<SizeDTO> getSizeByChartType(String chartType) {

        if (chartType == null || chartType.isEmpty()) {
            throw new IllegalArgumentException("chartType cannot be empty");
        }

        // Chuyển: "men size" → "MEN_SIZE"
        String formatted = chartType.replaceAll("[-\\s]", "_").toUpperCase();

        SizeChartType type;
        try {
            type = SizeChartType.valueOf(formatted);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid chart type: " + chartType);
        }

        List<Size> sizes = sizeRepository.findAllByChartTypeAndIsActiveTrue(type);

        return sizes.stream()
                .map(size -> new SizeDTO(
                        size.getId(),
                        size.getName(),
                        size.getSortOrder(),
                        size.getChartType(),
                        size.getIsActive()
                ))
                .toList();
    }



    public Size createSize(Size request) {

        if (request.getName() == null || request.getChartType() == null) {
            throw (new IllegalArgumentException("Invalid request , missing name and chart type"));
        }
        Size size = Size.builder()
                .name(request.getName())
                .chartType(request.getChartType())
                .sortOrder(request.getSortOrder())
                .isActive(request.getIsActive())
                .build();
        return sizeRepository.save(size);
    }



    public Size updateSize(@Valid Size request , long id) {
        Size size = sizeRepository.findSizeById(id);
        if(request.getName() != null) { size.setName(request.getName()); }
        if (request.getChartType() != null) { size.setChartType(request.getChartType()); }
        if(request.getSortOrder() != null) { size.setSortOrder(request.getSortOrder()); }


        return sizeRepository.save(size);
    }

    public void deleteSizeById(Long id) {
        Size size= sizeRepository.findSizeById(id);
        size.setIsActive(false);
        sizeRepository.save(size);
    }
}