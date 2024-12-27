package edu.tamu.scholars.discovery.etl.extract;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.tdb1.TDB1;
import org.apache.jena.tdb1.TDB1Factory;

import edu.tamu.scholars.discovery.etl.model.Data;

@Slf4j
public class TdbSparqlExtractor extends AbstractSparqlExtractor {

    private final Dataset dataset;

    public TdbSparqlExtractor(Data data) {
        super(data);

        String directory = this.properties.getOrDefault("directory", "triplestore");

        TDB1.getContext().setTrue(TDB1.symUnionDefaultGraph);
        this.dataset = TDB1Factory.createDataset(directory);
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
