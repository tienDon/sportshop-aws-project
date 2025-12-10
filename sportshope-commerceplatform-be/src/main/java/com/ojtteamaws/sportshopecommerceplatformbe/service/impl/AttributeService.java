package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.Attribute;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.AttributeValue;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.AttributeRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.AttributeValueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;

    public AttributeService(AttributeRepository attributeRepository, AttributeValueRepository attributeValueRepository) {
        this.attributeRepository = attributeRepository;
        this.attributeValueRepository = attributeValueRepository;
    }

    public List<Attribute> getAllAttributes() {
        return attributeRepository.findAll();
    }

    public Attribute createAttribute(String name, String code) {
        Attribute attribute = Attribute.builder()
                .name(name)
                .code(code)
                .build();
        return attributeRepository.save(attribute);
    }

    public AttributeValue createAttributeValue(Long attributeId, String value , Integer softoder) {
        Optional<Attribute> attributeOpt = attributeRepository.findById(attributeId);
        if (attributeOpt.isEmpty()) {
            throw new RuntimeException("Attribute not found");
        }

        AttributeValue attributeValue = AttributeValue.builder()
                .value(value)
                .sortOrder( softoder )

                .attribute(attributeOpt.get())
                .build();

        return attributeValueRepository.save(attributeValue);
    }

    public List<AttributeValue> getAttributeValuesByAttributeId(Long attributeId) {
        return attributeValueRepository.findByAttributeId(attributeId);
    }

    public List<Attribute> getAttributesWithValues(List<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return attributeRepository.findAll();
        }
        return attributeRepository.findByCodeIn(codes);
    }

//    @Transactional
//    public void deleteAllAttributes() {
//        attributeRepository.deleteAll();
//    }
//
//    @Transactional
//    public void deleteAttribute(Long attributeId) {
//        attributeRepository.deleteById(attributeId);
//    }

    // Delete Attribute
    public void deleteAttribute(Long id) {
        attributeRepository.deleteById(id);
    }

    // Update Attribute
    public Attribute updateAttribute(Long id, String name, String code) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));

        if (name != null) attribute.setName(name);
        if (code != null) attribute.setCode(code);

        return attributeRepository.save(attribute);
    }

    // Update Attribute Value
    public AttributeValue updateAttributeValue(Long id, String value, Integer sortOrder) {
        AttributeValue attrValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute Value not found"));

        if (value != null) attrValue.setValue(value);
        if (sortOrder != null) attrValue.setSortOrder(sortOrder);

        return attributeValueRepository.save(attrValue);
    }

    // Delete Attribute Value
    public void deleteAttributeValue(Long id) {
        attributeValueRepository.deleteById(id);
    }
}
