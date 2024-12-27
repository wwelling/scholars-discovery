package edu.tamu.scholars.discovery.etl.extract;

import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.tdb2.TDB2Factory;

import edu.tamu.scholars.discovery.etl.model.Data;

@Slf4j
public class TdbSparqlExtractor extends AbstractSparqlExtractor {

    private Dataset dataset;

    public TdbSparqlExtractor(Data data) {
        super(data);
    }

    @Override
    public void init(Map<String, String> propertyOverrides) {
        super.init(propertyOverrides);

        String directory = properties.containsKey("directory")
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
    protected QueryExecution createQueryExecution(String query) {
        if (Objects.isNull(dataset)) {
            throw new IllegalStateException("Dataset must be connected.");
        }
        return QueryExecutionFactory.create(query, dataset);
    }

}
