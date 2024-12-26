package edu.tamu.scholars.discovery.etl.transform;

import java.util.Map;

public class RdfToSolrDocumentTransformer implements DataTransformer<Map<String, Object>, Map<String, Object>> {

    private final Map<String, String> properties;

    public RdfToSolrDocumentTransformer(Map<String, String> properties) {
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
    public Map<String, Object> transform(Map<String, Object> data) {
        return data;
    }

}
