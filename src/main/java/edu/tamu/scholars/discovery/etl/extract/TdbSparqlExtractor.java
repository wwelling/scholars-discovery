package edu.tamu.scholars.discovery.etl.extract;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.triplestore.TdbTriplestore;

@Slf4j
public class TdbSparqlExtractor extends AbstractSparqlExtractor {

    private final TdbTriplestore triplestore;

    public TdbSparqlExtractor(Data data) {
        super(data);

        final String directory = this.properties
            .getOrDefault("directory", "triplestore");

        this.triplestore = TdbTriplestore.of(directory);
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
