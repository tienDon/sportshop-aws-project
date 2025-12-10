package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.controller.ColorController;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ColorDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Color;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Sport;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.ColorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService {

    @Autowired
    ColorRepository colorRepository;

    public List<ColorDTO> getAllColors() {
        return colorRepository.findAllByOrderByIdAsc()
                .stream()
                .map(c -> new ColorDTO(c.getId(), c.getName(), c.getHexCode()))
                .toList();
    }


    public Color createColor(@Valid Color request) {


        if(request.getName() == null) {
            throw new RuntimeException(" Name k dc bỏ trống");

        }
        if(colorRepository.existsByName(request.getName())) {
            throw new RuntimeException(" Name đa tồn tại");
        }


        Color color = Color.builder()
                .name(request.getName())
                .hexCode(request.getHexCode())
                .build();

        return colorRepository.save(color);


    }

    public Color updateColor(@Valid Color request , long id) {
        Color color = colorRepository.getColorById(id);
        if(request.getName() != null) { color.setName(request.getName()); }
        if (request.getHexCode() != null) { color.setHexCode(request.getHexCode()); }

        return colorRepository.save(color);
    }

    public void deleteColorById(long id) {
        colorRepository.deleteById(id);
    }

}
