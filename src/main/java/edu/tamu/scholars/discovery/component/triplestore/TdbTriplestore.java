package edu.tamu.scholars.discovery.component.triplestore;

import java.time.Duration;
import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.tdb1.TDB1;
import org.apache.jena.tdb1.TDB1Factory;

import edu.tamu.scholars.discovery.config.model.TriplestoreConfig;

@Slf4j
public class TdbTriplestore extends AbstractTriplestore {

    private final TriplestoreConfig config;

    private Dataset dataset;

    public TdbTriplestore(TriplestoreConfig config) {
        this.config = config;
    }

    @Override
    public QueryExecution createQueryExecution(String query) {
        return QueryExecutionFactory.create(query, dataset);
    }

    @Override
    public QueryExecution createQueryExecution(Query query) {
        return QueryExecutionFactory.create(query, dataset);
    }

    @Override
    public void init() {
        final Instant start = Instant.now();
        log.info("Intializing {}", config.getType().getSimpleName());
        TDB1.getContext().setTrue(TDB1.symUnionDefaultGraph);
        dataset = TDB1Factory.createDataset(config.getDirectory());
        log.info("{} ready. {} seconds",
            config.getType().getSimpleName(),
            Duration.between(start, Instant.now()).toMillis() / 1000.0);
    }

    @Override
    public void destroy() {
        dataset.close();
    }

}
