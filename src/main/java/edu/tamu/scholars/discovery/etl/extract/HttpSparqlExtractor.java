package edu.tamu.scholars.discovery.etl.extract;

import java.util.List;
import java.util.Map;

import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.etl.model.DataField;

public class HttpSparqlExtractor implements DataExtractor<Map<String, Object>> {

    private final Map<String, String> properties;

    public HttpSparqlExtractor(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void init() {

    }

    @Override
    public void preProcess(List<DataField> fields) {

    }

    @Override
    public void postProcess(List<DataField> fields) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public Flux<Map<String, Object>> extract() {
        return null;
    }

    @Override
    public Map<String, Object> extract(String subject) {
        return null;
    }

}
