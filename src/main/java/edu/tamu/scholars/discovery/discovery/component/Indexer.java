package edu.tamu.scholars.discovery.discovery.component;

import java.util.Collection;
import java.util.Map;

import edu.tamu.scholars.discovery.discovery.model.AbstractIndexDocument;

/**
 * 
 */
public interface Indexer {

    public void init();

    public void index(Collection<Map<String, Object>> documents);

    public void index(Map<String, Object> document);

    public void optimize();

    public Class<AbstractIndexDocument> type();

}
