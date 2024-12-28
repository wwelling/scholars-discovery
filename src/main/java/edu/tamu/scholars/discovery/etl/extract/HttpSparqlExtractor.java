package edu.tamu.scholars.discovery.etl.extract;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.triplestore.HttpTriplestore;

@Slf4j
public class HttpSparqlExtractor extends AbstractSparqlExtractor {

    private final HttpTriplestore triplestore;

    public HttpSparqlExtractor(Data data) {
        super(data);

        final String url = this.properties
            .getOrDefault("url", "http://localhost:8080/vivo/api/sparqlQuery");

        this.triplestore = HttpTriplestore.of(url);
    }

    @Override
    protected QueryExecution createQueryExecution(String query) {
        return triplestore.createQueryExecution(query);
    }

}
