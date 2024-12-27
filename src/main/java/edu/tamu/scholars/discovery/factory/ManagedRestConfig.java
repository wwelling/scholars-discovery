package edu.tamu.scholars.discovery.factory;

import static edu.tamu.scholars.discovery.utility.TypeUtility.as;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManagedRestConfig {

    public static final String MAX_CONN_TOTAL_PROPERTY_NAME = "maxConnTotal";
    public static final String MAX_CONN_PER_ROUTE_PROPERTY_NAME = "maxConnPerRoute";
    public static final String TIME_TO_LIVE_PROPERTY_NAME = "timeToLive";
    public static final String EVICT_IDLE_CONNECTIONS_PROPERTY_NAME = "evictIdleConnections";
    public static final String CONNECT_TIMEOUT_PROPERTY_NAME = "connectTimeout";
    public static final String READ_TIMEOUT_PROPERTY_NAME = "readTimeout";

    @Builder.Default
    private int maxConnTotal = 5;

    @Builder.Default
    private int maxConnPerRoute = 1;

    @Builder.Default
    private int timeToLive = 5;

    @Builder.Default
    private int evictIdleConnections = 1;

    @Builder.Default
    private int connectTimeout = 5;

    @Builder.Default
    private int readTimeout = 15;

    public ManagedRestConfig withMaxConnTotal(String value) {
        this.maxConnTotal = as(value, this.maxConnTotal);

        return this;
    }

    public ManagedRestConfig withMaxConnPerRoute(String value) {
        this.maxConnPerRoute = as(value, this.maxConnPerRoute);

        return this;
    }

    public ManagedRestConfig withTimeToLive(String value) {
        this.timeToLive = as(value, this.timeToLive);

        return this;
    }

    public ManagedRestConfig withEvictIdleConnections(String value) {
        this.evictIdleConnections = as(value, this.evictIdleConnections);

        return this;
    }

    public ManagedRestConfig withConnectTimeout(String value) {
        this.connectTimeout = as(value, this.connectTimeout);

        return this;
    }

    public ManagedRestConfig withReadTimeout(String value) {
        this.readTimeout = as(value, this.readTimeout);

        return this;
    }

}
