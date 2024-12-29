package edu.tamu.scholars.discovery.etl.extract;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;

import edu.tamu.scholars.discovery.component.triplestore.Triplestore;
import edu.tamu.scholars.discovery.etl.model.Data;

@Slf4j
public class TriplestoreExtractor extends AbstractTriplestoreExtractor {

    private final Triplestore triplestore;

    public TriplestoreExtractor(Data data, Triplestore triplestore) {
        super(data);
        this.triplestore = triplestore;
    }

    @Override
    public void destroy() {
        triplestore.destroy();
    }

    @Override
    protected QueryExecution createQueryExecution(String query) {
        return triplestore.createQueryExecution(query);
    }

}
