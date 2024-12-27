package edu.tamu.scholars.discovery.etl.extract;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.jena.query.Dataset;
import org.apache.jena.tdb2.TDB2Factory;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.etl.model.CollectionSource;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;

public class TdbSparqlExtractor implements DataExtractor<Map<String, Object>> {

    private final Map<String, String> properties;

    private final CollectionSource collectionSource;

    private final Set<DataField> fields;

    private Dataset dataset;

    public TdbSparqlExtractor(Data data) {
        this.properties = data.getExtractor().getAttributes();
        this.collectionSource = data.getCollectionSource();
        this.fields = data.getFields();
    }

    @Override
    public void init() {
        final String directory = properties.containsKey("directory")
                ? properties.get("directory")
                : "triplestore";

        dataset = TDB2Factory.connectDataset(directory);
    }

    @Override
    public void destroy() {
        if (Objects.nonNull(dataset)) {
            dataset.close();
        }
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
