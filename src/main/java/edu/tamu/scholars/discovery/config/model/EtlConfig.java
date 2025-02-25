package edu.tamu.scholars.discovery.config.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "discovery.etl")
public class EtlConfig {

    private boolean enabled = true;

    private int batchSize = 10000;

    private List<String> data = new ArrayList<>();

    private Map<String, Map<String, String>> overrides = new HashMap<>();

}
