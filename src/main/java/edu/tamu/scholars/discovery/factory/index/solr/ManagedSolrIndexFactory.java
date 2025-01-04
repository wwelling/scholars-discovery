package edu.tamu.scholars.discovery.factory.index.solr;

import static edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexConfig.COLLECTION_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexConfig.CONNECTION_TIMEOUT_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexConfig.HOST_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexConfig.IDLE_TIMEOUT_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexConfig.MAX_CONNECTIONS_PER_HOST_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexConfig.OPERATOR_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexConfig.PARSER_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexConfig.REQUEST_TIMEOUT_PROPERTY_NAME;
import static edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexConfig.ZONE_PROPERTY_NAME;

import java.util.Map;

import edu.tamu.scholars.discovery.config.model.IndexConfig;

public class ManagedSolrIndexFactory {

    private ManagedSolrIndexFactory() {

    }

    public static ManagedSolrIndex of() {
        return ManagedSolrIndex.with(ManagedSolrIndexConfig.builder().build());
    }

    public static ManagedSolrIndex of(IndexConfig indexConfig) {
        ManagedSolrIndexConfig config = ManagedSolrIndexConfig.builder()
            .build()
            .withHost(indexConfig.getHost())
            .withCollection(indexConfig.getCollection())
            .withOperator(indexConfig.getOperator())
            .withParser(indexConfig.getParser())
            .withZone(indexConfig.getZone())
            .withConnectionTimeout(indexConfig.getConnectionTimeout())
            .withIdleTimeout(indexConfig.getIdleTimeout())
            .withMaxConnectionPerHost(indexConfig.getMaxConnectionPerHost())
            .withRequestTimeout(indexConfig.getRequestTimeout());

        return ManagedSolrIndex.with(config);
    }

    public static ManagedSolrIndex of(Map<String, String> properties) {
        ManagedSolrIndexConfig config = ManagedSolrIndexConfig.builder()
            .build()
            .withHost(properties.get(HOST_PROPERTY_NAME))
            .withCollection(properties.get(COLLECTION_PROPERTY_NAME))
            .withOperator(properties.get(OPERATOR_PROPERTY_NAME))
            .withParser(properties.get(PARSER_PROPERTY_NAME))
            .withZone(properties.get(ZONE_PROPERTY_NAME))
            .withConnectionTimeout(properties.get(CONNECTION_TIMEOUT_PROPERTY_NAME))
            .withIdleTimeout(properties.get(IDLE_TIMEOUT_PROPERTY_NAME))
            .withMaxConnectionPerHost(properties.get(MAX_CONNECTIONS_PER_HOST_PROPERTY_NAME))
            .withRequestTimeout(properties.get(REQUEST_TIMEOUT_PROPERTY_NAME));

        return ManagedSolrIndex.with(config);
    }

    public static ManagedSolrIndex of(ManagedSolrIndexConfig config) {
        return ManagedSolrIndex.with(config);
    }

}
