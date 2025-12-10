package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.SportDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Brand;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Size;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Sport;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.SportRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportService {

    @Autowired
    private SportRepository sportRepository;

    public Sport createSport(@Valid Sport request) {


        if(request.getName() == null || request.getSlug()==null) {
            throw new RuntimeException("Slug và Name k dc bỏ trống");

        }
        if(sportRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug và Name đax tồn tại");
        }


        Sport sport = Sport.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .isActive(request.getIsActive())
                .sortOrder(request.getSortOrder())
                .build();

        return sportRepository.save(sport);


    }

    public List<SportDTO> getAllSports() {
        return sportRepository.findSportByIsActiveTrue()
                .stream()
                .map(s -> new SportDTO(s.getId(), s.getName(), s.getSlug(), s.getIsActive(), s.getSortOrder()))
                .toList();
    }


    public Sport updateSport(@Valid Sport request , long id) {
        Sport sport = sportRepository.findSportById(id);
        if(request.getName() != null) { sport.setName(request.getName()); }
        if (request.getSlug() != null) { sport.setSlug(request.getSlug()); }
        if(request.getSortOrder() != null) { sport.setSortOrder(request.getSortOrder()); }


        return sportRepository.save(sport);
    }

    public void deleteSportById(Long id) {
        Sport sport = sportRepository.findSportById(id);
        sport.setIsActive(false);
        sportRepository.save(sport);
    }
}
