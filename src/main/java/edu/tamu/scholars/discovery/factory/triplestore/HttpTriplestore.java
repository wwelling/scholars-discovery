package edu.tamu.scholars.discovery.factory.triplestore;

import java.util.Map;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTPBuilder;

public class HttpTriplestore extends AbstractTriplestore {

    private final QueryExecutionHTTPBuilder builder;

    private HttpTriplestore(String url) {
        this.builder = QueryExecution.service(url);
    }

    @Override
    protected QueryExecution createQueryExecution(String query) {
        return this.builder.query(query).build();
    }

    public static HttpTriplestore of(Map<String, String> attributes) {
        String url = attributes.getOrDefault("url", "http://localhost:8080/api/sparqlQuery");

        return new HttpTriplestore(url);
    }

}
