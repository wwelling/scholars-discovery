package edu.tamu.scholars.middleware.discovery.component;

import reactor.core.publisher.Flux;

import edu.tamu.scholars.middleware.discovery.model.AbstractIndexDocument;
import edu.tamu.scholars.middleware.discovery.model.Individual;

/**
 * 
 */
public interface Harvester {

    public Flux<Individual> harvest();

    public Individual harvest(String subject);

    public Class<AbstractIndexDocument> type();

}
