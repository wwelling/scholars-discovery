package edu.tamu.scholars.discovery.component.triplestore;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTPBuilder;

import edu.tamu.scholars.discovery.config.model.TriplestoreConfig;

@Slf4j
public class HttpTriplestore extends AbstractTriplestore {

    private final TriplestoreConfig config;

    private QueryExecutionHTTPBuilder builder;

    public HttpTriplestore(TriplestoreConfig config) {
        this.config = config;
    }

    @Override
    public QueryExecution createQueryExecution(Query query) {
        return this.builder.query(query).build();
    }

    @Override
    public QueryExecution createQueryExecution(String query) {
        return this.builder.query(query).build();
    }

    @Override
    public void init() {
        this.builder = QueryExecution.service(config.getUrl());
    }

    @Override
    public void destroy() {
        // no-op
    }

}
