package edu.tamu.scholars.discovery.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.tamu.scholars.discovery.config.model.IndexConfig;
import edu.tamu.scholars.discovery.factory.index.Index;
import edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexFactory;

@Configuration
public class SearchEngineConfig {

    @Bean
    @ConditionalOnProperty(name = "discovery.index.type", havingValue = "solr")
    Index<?> solrIndex(IndexConfig config) {
        return ManagedSolrIndexFactory.of(config);
    }

}
