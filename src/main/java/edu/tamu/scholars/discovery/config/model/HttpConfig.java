package edu.tamu.scholars.discovery.config.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "discovery.http")
public class HttpConfig {

    // TODO: add validations and @Validated to the class

    private int maxTotalConnections = 50;

    private int maxConnectionsPerRoute = 20;

    /** connection time to live in minutes */
    private int timeToLive = 5;

    /** minutes before evicting idle connections */
    private int evictIdleConnections = 2;

    private int connectTimeout = 5;

    private int readTimeout = 30;

}
