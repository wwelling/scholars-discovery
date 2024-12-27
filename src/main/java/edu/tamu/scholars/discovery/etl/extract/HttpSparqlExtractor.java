package edu.tamu.scholars.discovery.etl.extract;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QueryExecution;

import edu.tamu.scholars.discovery.etl.model.Data;

@Slf4j
public class HttpSparqlExtractor extends AbstractSparqlExtractor {

    private final String serviceUrl;

    public HttpSparqlExtractor(Data data) {
        super(data);

        this.serviceUrl = properties.getOrDefault("url", "http://localhost:8080/vivo/api/sparqlQuery");
    }

    @Override
    protected QueryExecution createQueryExecution(String query) {
        if (StringUtils.isEmpty(serviceUrl)) {
            throw new IllegalStateException("Service URL must not be null or empty.");
        }

        return QueryExecution.service(serviceUrl)
            .query(query)
            .build();
    }

}
