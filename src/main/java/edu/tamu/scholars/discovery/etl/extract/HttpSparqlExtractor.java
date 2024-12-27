package edu.tamu.scholars.discovery.etl.extract;

import java.util.Map;
import java.util.Set;

import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.etl.model.CollectionSource;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;

public class HttpSparqlExtractor implements DataExtractor<Map<String, Object>> {

    private final Map<String, String> properties;

    private final CollectionSource collectionSource;

    private final Set<DataField> fields;

    public HttpSparqlExtractor(Data data) {
        this.properties = data.getExtractor().getAttributes();
        this.collectionSource = data.getCollectionSource();
        this.fields = data.getFields();
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
        return Flux.empty();
    }

    @Override
    public Map<String, Object> extract(String subject) {
        return Map.of();
    }

}
