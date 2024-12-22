package edu.tamu.scholars.discovery.discovery.component;

import java.util.Collection;

import edu.tamu.scholars.discovery.discovery.model.AbstractIndexDocument;
import edu.tamu.scholars.discovery.discovery.model.Individual;

/**
 * 
 */
public interface Indexer {

    public void init();

    public void index(Collection<Individual> documents);

    public void index(Individual document);

    public void optimize();

    public Class<AbstractIndexDocument> type();

}
