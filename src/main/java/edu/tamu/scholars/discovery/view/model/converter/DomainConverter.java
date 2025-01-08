package edu.tamu.scholars.discovery.view.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DomainConverter implements AttributeConverter<JsonNode, String> {

    private final ObjectMapper mapper;

    DomainConverter() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        try {
            return mapper.readTree(dbData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
