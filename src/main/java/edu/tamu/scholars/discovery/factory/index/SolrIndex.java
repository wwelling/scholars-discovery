package edu.tamu.scholars.discovery.factory.index;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import edu.tamu.scholars.discovery.factory.rest.ManagedRestTemplate;
import edu.tamu.scholars.discovery.factory.rest.ManagedRestTemplateFactory;

@Slf4j
public class SolrIndex implements Index<JsonNode, JsonNode, ResponseEntity<JsonNode>, JsonNode> {

    private final String host;

    private final String collection;

    private final ManagedRestTemplate restTemplate;

    private final Http2SolrClient solrClient;

    private SolrIndex(String host, String collection) {
        this.host = host;
        this.collection = collection;
        this.restTemplate = ManagedRestTemplateFactory.of()
            .withErrorHandler(new SolrResponseErrorHandler());

        this.solrClient = new Http2SolrClient.Builder("http://localhost:8983/solr/scholars-discovery").build();
    }

    @Override
    public Stream<JsonNode> fields() {
        String url = getUrl("schema", "fields");

        ResponseEntity<JsonNode> response = this.restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Iterable<JsonNode> iterable = () -> response.getBody().get("fields").iterator();

            return StreamSupport.stream(iterable.spliterator(), false);
        }

        return Stream.empty();
    }

    @Override
    public Stream<JsonNode> copyFields() {
        String url = getUrl("schema", "copyfields");

        ResponseEntity<JsonNode> response = this.restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Iterable<JsonNode> iterable = () -> response.getBody().get("copyFields").iterator();

            return StreamSupport.stream(iterable.spliterator(), false);
        }

        return Stream.empty();
    }

    @Override
    public ResponseEntity<JsonNode> schema(JsonNode schema) {
        String url = getUrlWithQuery("schema", "commit=true");

        HttpEntity<JsonNode> requestEntity = getHttpEntity(schema);

        return this.restTemplate.postForEntity(url, requestEntity, JsonNode.class);
    }

    @Override
    public ResponseEntity<JsonNode> update(JsonNode update) {
        String url = getUrlWithQuery("update", "commit=true");

        System.out.println("\n\n" + update + "\n\n");

        HttpEntity<JsonNode> requestEntity = getHttpEntity(update);

        return this.restTemplate.postForEntity(url, requestEntity, JsonNode.class);
    }

    @Override
    public void close() {
        this.restTemplate.destroy();
    }

    private String getUrl(String... paths) {
        String baseUrl = removeEnd(this.host, "/");
        String path = join("/", paths);

        return join("/", baseUrl, this.collection, path);
    }

    private String getUrlWithQuery(String path, String queryParams) {
        return format("%s?%s", getUrl(path), removeStart(queryParams, "?"));
    }

    private <S> HttpEntity<S> getHttpEntity(S requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(requestBody, headers);
    }

    private class SolrResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode()
                .isError();
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            // TODO: log error
        }

    }

    public static SolrIndex of(Map<String, String> attributes) {
        String host = attributes.getOrDefault("host", "http://localhost:8983/solr");
        String collection = attributes.getOrDefault("collection", "scholars-discovery");

        return new SolrIndex(host, collection);
    }

}
