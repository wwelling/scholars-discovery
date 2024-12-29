package edu.tamu.scholars.discovery.component.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.config.model.MapperConfig;

@Slf4j
public class JsonMapper implements Mapper<JsonNode> {

    private final MapperConfig config;

    private final ObjectMapper objectMapper;

    public JsonMapper(MapperConfig config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
    }

    public JsonNode valueToTree(Object value) {
        return this.objectMapper.valueToTree(value);
    }

}
