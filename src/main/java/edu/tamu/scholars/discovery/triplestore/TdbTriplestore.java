package edu.tamu.scholars.discovery.triplestore;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb1.TDB1;
import org.apache.jena.tdb1.TDB1Factory;
import reactor.core.publisher.Flux;

@Slf4j
public class TdbTriplestore implements Triplestore {

    private final Dataset dataset;
    private final Object lock = new Object();

    private TdbTriplestore(String directory) {
        TDB1.getContext().setTrue(TDB1.symUnionDefaultGraph);
        this.dataset = TDB1Factory.createDataset(directory);
    }

    @Override
    public Flux<Triple> execConstructTriples(Query query) {
        beginTransaction();
        QueryExecution qe = QueryExecutionFactory.create(query, dataset);

        return Flux.fromIterable(qe::execConstructTriples)
                .doFinally(signal -> {
                    try {
                        qe.close();
                    } finally {
                        endTransaction();
                    }
                });
    }

    @Override
    public Model execConstruct(String query) {
        beginTransaction();
        try (QueryExecution qe = QueryExecutionFactory.create(query, dataset)) {
            return qe.execConstruct();
        } finally {
            endTransaction();
        }
    }

    @Override
    public Model execConstruct(Query query) {
        beginTransaction();
        try (QueryExecution qe = QueryExecutionFactory.create(query, dataset)) {
            return qe.execConstruct();
        } finally {
            endTransaction();
        }
    }

    @Override
    public void destroy() {
        endTransaction();
        dataset.close();
    }

    private void beginTransaction() {
        synchronized (lock) {
            if (!dataset.isInTransaction()) {
                dataset.begin(ReadWrite.READ);
            }
        }
    }

    private void endTransaction() {
        synchronized (lock) {
            if (dataset.isInTransaction()) {
                dataset.end();
            }
        }
    }

    public static TdbTriplestore of(String directory) {
        return new TdbTriplestore(directory);
    }

}
