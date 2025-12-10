package com.ojtteamaws.sportshopecommerceplatformbe.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        // join list thành 1 chuỗi "a,b,c"
        return String.join(SPLIT_CHAR, attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new ArrayList<>();
        }
        // tách "a,b,c" → List<String>
        return new ArrayList<>(
                Arrays.stream(dbData.split(SPLIT_CHAR))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList()
        );
    }
}
