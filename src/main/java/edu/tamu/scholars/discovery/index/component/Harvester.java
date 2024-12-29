package edu.tamu.scholars.discovery.index.component;

import java.io.IOException;
import java.util.Map;

import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.index.model.AbstractIndexDocument;


public interface Harvester {

    public Flux<Map<String, Object>> harvest() throws IOException;

    public Map<String, Object> harvest(String subject);

    public Class<AbstractIndexDocument> type();

}
