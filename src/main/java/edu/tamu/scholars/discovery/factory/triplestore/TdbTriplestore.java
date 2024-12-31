package edu.tamu.scholars.discovery.factory.triplestore;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.tdb1.TDB1;
import org.apache.jena.tdb1.TDB1Factory;

@Slf4j
public class TdbTriplestore extends AbstractTriplestore {

    private final Dataset dataset;

    private TdbTriplestore(String directory) {
        TDB1.getContext().setTrue(TDB1.symUnionDefaultGraph);
        this.dataset = TDB1Factory.createDataset(directory);
    }

    @Override
    public void close() {
        this.dataset.close();
    }

    @Override
    protected QueryExecution createQueryExecution(String query) {
        return QueryExecutionFactory.create(query, dataset);
    }

    public static TdbTriplestore of(String directory) {
        return new TdbTriplestore(directory);
    }

}
