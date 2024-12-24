package edu.tamu.scholars.discovery.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Injectable discovery configuration to specify http properties.
 * 
 * <p>
 * See `discovery.http` in src/main/resources/application.yml.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "discovery.http")
public class HttpConfig {

    private int maxTotalConnections = 50;

    private int maxConnectionsPerRoute = 20;

    /** connection time to live in minutes */
    private int timeToLive = 5;

    /** minutes before evicting idle connections */
    private int evictIdleConnections = 2;

    private int connectTimeout = 5;

    private int readTimeout = 30;

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public int getMaxConnectionsPerRoute() {
        return maxConnectionsPerRoute;
    }

    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getEvictIdleConnections() {
        return evictIdleConnections;
    }

    public void setEvictIdleConnections(int evictIdleConnections) {
        this.evictIdleConnections = evictIdleConnections;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

}
