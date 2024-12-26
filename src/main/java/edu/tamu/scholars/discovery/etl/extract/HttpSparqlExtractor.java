package edu.tamu.scholars.discovery.etl.extract;

import java.util.Map;

import reactor.core.publisher.Flux;

public class HttpSparqlExtractor implements DataExtractor<Map<String, Object>> {

    public HttpSparqlExtractor() {

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
    public Flux<Map<String, Object>> extract() {
        return null;
    }

    @Override
    public Map<String, Object> extract(String subject) {
        return null;
    }

}
