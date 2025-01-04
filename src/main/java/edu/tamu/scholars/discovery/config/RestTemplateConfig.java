package edu.tamu.scholars.discovery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import edu.tamu.scholars.discovery.config.model.HttpConfig;
import edu.tamu.scholars.discovery.factory.rest.ManagedRestTemplateFactory;

@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate(HttpConfig config) {
        return ManagedRestTemplateFactory.of(config);
    }

}
