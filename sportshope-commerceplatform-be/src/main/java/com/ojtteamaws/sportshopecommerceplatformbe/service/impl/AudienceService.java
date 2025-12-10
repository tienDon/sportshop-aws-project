package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.AudienceDTO;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.Audience;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.AudienceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AudienceService {

    private final AudienceRepository audienceRepository;

    public AudienceService(AudienceRepository audienceRepository) {
        this.audienceRepository = audienceRepository;
    }

    public List<AudienceDTO> getAllAudiences() {
        return audienceRepository.findAll()
                .stream()
                .map(a -> new AudienceDTO(
                        a.getId(),
                        a.getName(),
                        a.getSlug(),
                        a.getSortOrder()
                ))
                .toList();
    }

    public AudienceDTO getAudienceById(Long id) {
        Audience a = audienceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audience not found"));
        return new AudienceDTO(
                a.getId(),
                a.getName(),
                a.getSlug(),
                a.getSortOrder()
        );
    }


    public Audience createAudience(String name, String slug ,Integer sortOrder ) {
        audienceRepository.findByNameOrSlug(name, slug).ifPresent(a -> {
            throw new RuntimeException("Audience with the same name or slug already exists");
        });

        Audience audience = Audience.builder()
                .name(name)
                .slug(slug)
                .sortOrder(sortOrder)
                .build();

        return audienceRepository.save(audience);
    }

    public Audience updateAudience(Long id, String name, String slug , Integer sortOrder) {
        Audience audience = audienceRepository.findAudienceById(id);
        if (name != null) audience.setName(name);
        if (slug != null) audience.setSlug(slug);
        if (sortOrder != null) audience.setSortOrder(sortOrder);

        return audienceRepository.save(audience);
    }

    public void deleteAudience(Long id) {
        Audience audience = audienceRepository.findAudienceById(id);
        audienceRepository.delete(audience);
    }
}
