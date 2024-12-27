package edu.tamu.scholars.discovery.factory;

import static edu.tamu.scholars.discovery.factory.ManagedRestConfig.CONNECT_TIMEOUT_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.ManagedRestConfig.EVICT_IDLE_CONNECTIONS_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.ManagedRestConfig.MAX_CONN_PER_ROUTE_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.ManagedRestConfig.MAX_CONN_TOTAL_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.ManagedRestConfig.READ_TIMEOUT_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.ManagedRestConfig.TIME_TO_LIVE_PROPERTY_NAME;

import java.util.Map;

public class ManagedRestTemplateFactory {

    private ManagedRestTemplateFactory() {

    }

    public static ManagedRestTemplate of() {
        return ManagedRestTemplate.with(ManagedRestConfig.builder().build());
    }

    public static ManagedRestTemplate of(Map<String, String> properties) {
        ManagedRestConfig config = ManagedRestConfig.builder().build()
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
