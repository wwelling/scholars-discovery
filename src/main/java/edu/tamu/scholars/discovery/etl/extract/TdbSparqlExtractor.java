package edu.tamu.scholars.discovery.etl.extract;

import java.util.Map;
import java.util.Objects;

import org.apache.jena.query.Dataset;
import org.apache.jena.tdb2.TDB2;
import org.apache.jena.tdb2.TDB2Factory;
import reactor.core.publisher.Flux;

public class TdbSparqlExtractor implements DataExtractor<Map<String, Object>> {

    private final Map<String, String> properties;

    private Dataset dataset;

    public TdbSparqlExtractor(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void init() {
        final String directory = properties.containsKey("directory")
            ? properties.get("directory")
            : "triplestore";

        TDB2.getContext().setTrue(TDB2.symUnionDefaultGraph);
        dataset = TDB2Factory.connectDataset(directory);
    }

    @Override
    public void preProcess() {

    }

    @Override
    public void postProcess() {

    }

    @Override
    public void destroy() {
        if (Objects.nonNull(dataset)) {
            dataset.close();
        }
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
