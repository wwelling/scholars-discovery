package edu.tamu.scholars.discovery.discovery.component;

import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.discovery.model.AbstractIndexDocument;
import edu.tamu.scholars.discovery.discovery.model.Individual;

/**
 * 
 */
public interface Harvester {

    public Flux<Individual> harvest();

    public Individual harvest(String subject);

    public Class<AbstractIndexDocument> type();

}
