package edu.tamu.scholars.discovery.etl.load;

import java.util.Collection;
import java.util.Map;

public class SolrIndexLoader implements DataLoader<Map<String, Object>> {

    private final Map<String, String> properties;

    public SolrIndexLoader(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void init() {

    }

    @Override
    public void preProcess() {

    }

    @Override
    public void postProcess() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void load(Collection<Map<String, Object>> documents) {

    }

    @Override
    public void load(Map<String, Object> document) {

    }

}
