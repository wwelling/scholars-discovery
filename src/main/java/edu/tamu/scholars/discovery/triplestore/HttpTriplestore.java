package edu.tamu.scholars.discovery.triplestore;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTPBuilder;

@Slf4j
public class HttpTriplestore implements Triplestore {

    private final QueryExecutionHTTPBuilder builder;

    private HttpTriplestore(String url) {
        this.builder = QueryExecution.service(url);
    }

    @Override
    public QueryExecution createQueryExecution(String query) {
        return this.builder.query(query)
                .build();
    }

    public static HttpTriplestore of(String url) {
        return new HttpTriplestore(url);
    }

}
