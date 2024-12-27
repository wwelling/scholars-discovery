package edu.tamu.scholars.discovery.etl.extract;

import java.util.Map;
import java.util.Set;

import org.apache.jena.query.QueryExecution;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.etl.model.CollectionSource;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;

public abstract class AbstractSparqlExtractor implements DataExtractor<Map<String, Object>> {

    protected final Map<String, String> properties;

    protected final CollectionSource collectionSource;

    protected final Set<DataField> fields;

    protected AbstractSparqlExtractor(Data data) {
        this.properties = data.getExtractor().getAttributes();
        this.collectionSource = data.getCollectionSource();
        this.fields = data.getFields();
    }

    @Override
    public Flux<Map<String, Object>> extract() {
        return Flux.empty();
    }

    @Override
    public Map<String, Object> extract(String subject) {
        return Map.of();
    }

    protected abstract QueryExecution createQueryExecution(String query);

}
