package edu.tamu.scholars.discovery.discovery.component;

import java.util.Map;

import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.discovery.model.AbstractIndexDocument;

/**
 * 
 */
public interface Harvester {

    public Flux<Map<String, Object>> harvest();

    public Map<String, Object> harvest(String subject);

    public Class<AbstractIndexDocument> type();

}
