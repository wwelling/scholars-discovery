package edu.tamu.scholars.discovery.factory.index.solr;

import static edu.tamu.scholars.discovery.utility.TypeUtility.as;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManagedSolrIndexConfig {

    public static final String HOST_PROPERTY_NAME = "host";
    public static final String COLLECTION_PROPERTY_NAME = "collection";
    public static final String OPERATOR_PROPERTY_NAME = "operator";
    public static final String PARSER_PROPERTY_NAME = "parser";
    public static final String ZONE_PROPERTY_NAME = "zone";
    public static final String CONNECTION_TIMEOUT_PROPERTY_NAME = "connectionTimeout";
    public static final String IDLE_TIMEOUT_PROPERTY_NAME = "idleTimeout";
    public static final String MAX_CONNECTIONS_PER_HOST_PROPERTY_NAME = "maxConnectionsPerHost";
    public static final String REQUEST_TIMEOUT_PROPERTY_NAME = "requestTimeout";

    @Builder.Default
    private String host = "http://localhost:8983/solr";

    @Builder.Default
    private String collection = "scholars-discovery";

    @Builder.Default
    private String operator = "OR";

    @Builder.Default
    private String parser = "edismax";

    @Builder.Default
    private String zone = "America/Chicago";

    @Builder.Default
    private int connectionTimeout = 1;

    @Builder.Default
    private int idleTimeout = 2;

    @Builder.Default
    private int maxConnectionPerHost = 10;

    @Builder.Default
    private int requestTimeout = 5;

    public ManagedSolrIndexConfig withHost(String host) {
        this.host = host;

        return this;
    }

    public ManagedSolrIndexConfig withCollection(String collection) {
        this.collection = collection;

        return this;
    }

    public ManagedSolrIndexConfig withOperator(String operator) {
        this.operator = operator;

        return this;
    }

    public ManagedSolrIndexConfig withParser(String parser) {
        this.parser = parser;

        return this;
    }

    public ManagedSolrIndexConfig withZone(String zone) {
        this.zone = zone;

        return this;
    }

    public ManagedSolrIndexConfig withConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;

        return this;
    }

    public ManagedSolrIndexConfig withConnectionTimeout(String value) {
        this.connectionTimeout = as(value, this.connectionTimeout);

        return this;
    }

    public ManagedSolrIndexConfig withIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;

        return this;
    }

    public ManagedSolrIndexConfig withIdleTimeout(String value) {
        this.idleTimeout = as(value, this.idleTimeout);

        return this;
    }

    public ManagedSolrIndexConfig withMaxConnectionPerHost(int maxConnectionPerHost) {
        this.maxConnectionPerHost = maxConnectionPerHost;

        return this;
    }

    public ManagedSolrIndexConfig withMaxConnectionPerHost(String value) {
        this.maxConnectionPerHost = as(value, this.maxConnectionPerHost);

        return this;
    }

    public ManagedSolrIndexConfig withRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;

        return this;
    }

    public ManagedSolrIndexConfig withRequestTimeout(String value) {
        this.requestTimeout = as(value, this.requestTimeout);

        return this;
    }

}
