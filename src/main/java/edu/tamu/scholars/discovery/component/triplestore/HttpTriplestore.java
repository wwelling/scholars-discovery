package edu.tamu.scholars.discovery.component.triplestore;

import java.time.Duration;
import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTPBuilder;

import edu.tamu.scholars.discovery.config.model.TriplestoreConfig;

@Slf4j
public class HttpTriplestore extends AbstractTriplestore {

    private final TriplestoreConfig config;

    private final QueryExecutionHTTPBuilder builder;

    public HttpTriplestore(TriplestoreConfig config) {
        this.config = config;
        this.builder = QueryExecution.service(config.getUrl());
    }

    @Override
    public QueryExecution createQueryExecution(Query query) {
        return this.builder.query(query)
            .build();
    }

    @Override
    public QueryExecution createQueryExecution(String query) {
        return this.builder.query(query)
            .build();
    }

    @Override
    public void init() {
        Instant start = Instant.now();
        log.info("Initializing {}", config.getType().getSimpleName());
        log.info("{} ready. {} seconds",
            config.getType().getSimpleName(),
            Duration.between(start, Instant.now()).toMillis() / 1000.0);
    }

    @Override
    public void destroy() {
        // no-op
    }

}
