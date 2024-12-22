package edu.tamu.scholars.discovery.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import edu.tamu.scholars.discovery.config.model.HttpConfig;

@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder, HttpConfig config) {
        return builder
            .connectTimeout(Duration.ofSeconds(config.getConnectTimeout()))
            .readTimeout(Duration.ofSeconds(config.getReadTimeout()))
            .build();
    }

}
