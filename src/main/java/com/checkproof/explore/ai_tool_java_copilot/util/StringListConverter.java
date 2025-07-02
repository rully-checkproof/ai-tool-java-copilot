package com.checkproof.explore.ai_tool_java_copilot.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Converter
public class StringListConverter implements AttributeConverter<List<String>, Array> {

    @Override
    public Array convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        try {
            // Convert List<String> to String array for PostgreSQL
            String[] array = attribute.toArray(new String[0]);
            // Note: The actual Array creation will be handled by Hibernate/PostgreSQL driver
            return java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/ai_tool_java_copilot", "rully.kurniawan", "")
                .createArrayOf("text", array);
        } catch (SQLException e) {
            log.error("Error converting list to PostgreSQL array", e);
            return null;
        }
    }

    @Override
    public List<String> convertToEntityAttribute(Array dbData) {
        if (dbData == null) {
            return List.of();
        }
        try {
            String[] array = (String[]) dbData.getArray();
            return Arrays.asList(array);
        } catch (SQLException e) {
            log.error("Error converting PostgreSQL array to list", e);
            return List.of();
        }
    }
}
