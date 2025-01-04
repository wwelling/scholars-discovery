package edu.tamu.scholars.discovery.factory.rest;

import static edu.tamu.scholars.discovery.factory.rest.ManagedRestConfig.CONNECT_TIMEOUT_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.rest.ManagedRestConfig.EVICT_IDLE_CONNECTIONS_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.rest.ManagedRestConfig.MAX_CONN_PER_ROUTE_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.rest.ManagedRestConfig.MAX_CONN_TOTAL_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.rest.ManagedRestConfig.READ_TIMEOUT_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.rest.ManagedRestConfig.TIME_TO_LIVE_PROPERTY_NAME;

import java.util.Map;

import edu.tamu.scholars.discovery.config.model.HttpConfig;

public class ManagedRestTemplateFactory {

    private ManagedRestTemplateFactory() {

    }

    public static ManagedRestTemplate of() {
        return ManagedRestTemplate.with(ManagedRestConfig.builder().build());
    }

    public static ManagedRestTemplate of(HttpConfig httpConfig) {
        ManagedRestConfig config = ManagedRestConfig.builder()
            .build()
            .withMaxConnTotal(httpConfig.getMaxTotalConnections())
            .withMaxConnPerRoute(httpConfig.getMaxConnectionsPerRoute())
            .withTimeToLive(httpConfig.getTimeToLive())
            .withEvictIdleConnections(httpConfig.getEvictIdleConnections())
            .withConnectTimeout(httpConfig.getConnectTimeout())
            .withReadTimeout(httpConfig.getReadTimeout());

        return ManagedRestTemplate.with(config);
    }

    public static ManagedRestTemplate of(Map<String, String> properties) {
        ManagedRestConfig config = ManagedRestConfig.builder()
            .build()
            .withMaxConnTotal(properties.get(MAX_CONN_TOTAL_PROPERTY_NAME))
            .withMaxConnPerRoute(properties.get(MAX_CONN_PER_ROUTE_PROPERTY_NAME))
            .withTimeToLive(properties.get(TIME_TO_LIVE_PROPERTY_NAME))
            .withEvictIdleConnections(properties.get(EVICT_IDLE_CONNECTIONS_PROPERTY_NAME))
            .withConnectTimeout(properties.get(CONNECT_TIMEOUT_PROPERTY_NAME))
            .withReadTimeout(properties.get(READ_TIMEOUT_PROPERTY_NAME));

        return ManagedRestTemplate.with(config);
    }

    public static ManagedRestTemplate of(ManagedRestConfig config) {
        return ManagedRestTemplate.with(config);
    }

}
