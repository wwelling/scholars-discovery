package edu.tamu.scholars.discovery.component.json;

import lombok.extern.slf4j.Slf4j;

import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.config.model.MapperConfig;

@Slf4j
public class JsonMapper implements Mapper {

    private final MapperConfig config;

    public JsonMapper(MapperConfig config) {
        this.config = config;
    }

}
