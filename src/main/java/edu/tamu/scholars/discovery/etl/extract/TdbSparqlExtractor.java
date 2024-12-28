package edu.tamu.scholars.discovery.etl.extract;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Model;
import reactor.core.publisher.Flux;

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
    protected Flux<Triple> execConstructTriples(Query query) {
        return triplestore.execConstructTriples(query);
    }

    @Override
    protected Model execConstruct(Query query) {
        return triplestore.execConstruct(query);
    }

}
