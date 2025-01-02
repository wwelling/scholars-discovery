package edu.tamu.scholars.discovery.factory.index;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import edu.tamu.scholars.discovery.factory.rest.ManagedRestTemplate;
import edu.tamu.scholars.discovery.factory.rest.ManagedRestTemplateFactory;

@Slf4j
public class SolrIndex implements Index<JsonNode, JsonNode, JsonNode, SolrInputDocument> {

    private final String host;

    private final String collection;

    private final ManagedRestTemplate restTemplate;

    private final Http2SolrClient solrClient;

    private SolrIndex(String host, String collection) {
        this.host = host;
        this.collection = collection;
        this.restTemplate = ManagedRestTemplateFactory.of()
            .withErrorHandler(new SolrResponseErrorHandler());

        this.solrClient = new Http2SolrClient.Builder(getCollectionUrl())
            .withConnectionTimeout(1, TimeUnit.MINUTES)
            .withIdleTimeout(5, TimeUnit.MINUTES)
            .withMaxConnectionsPerHost(10)
            .withRequestTimeout(5, TimeUnit.MINUTES)
            .build();
    }

    @Override
    public Stream<JsonNode> fields() {
        return fields("fields");
    }

    @Override
    public Stream<JsonNode> copyFields() {
        return fields("copyFields");
    }

    @Override
    public JsonNode schema(JsonNode schema) {
        String url = getUrlWithQuery("schema", "commit=true");

        HttpEntity<JsonNode> requestEntity = getHttpEntity(schema);

        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity(url, requestEntity, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        // TODO: add error logging

        return JsonNodeFactory.instance.objectNode();
    }

    @Override
    public void update(Collection<SolrInputDocument> documents) {
        try {
            this.solrClient.add(documents);
            this.solrClient.commit();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.warn("Error updating Solr documents", e);
            log.info("Attempting batch documents individually");
            documents.forEach(this::update);
        }
    }

    @Override
    public void update(SolrInputDocument document) {
        try {
            this.solrClient.add(document);
            this.solrClient.commit();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error updating Solr document", e);
        }
    }

    @Override
    public void close() {
        this.restTemplate.destroy();
        this.solrClient.close();
    }

    private Stream<JsonNode> fields(String path) {
        String url = getUrl("schema", path.toLowerCase());

        ResponseEntity<JsonNode> response = this.restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Iterable<JsonNode> iterable = () -> response.getBody().get(path).iterator();

            return StreamSupport.stream(iterable.spliterator(), false);
        }

        // TODO: add error logging

        return Stream.empty();
    }

    private String getCollectionUrl() {
        String baseUrl = removeEnd(this.host, "/");

        return join("/", baseUrl, this.collection);
    }

    private String getUrl(String... paths) {
        String collectionUrl = getCollectionUrl();
        String path = join("/", paths);

        return join("/", collectionUrl, path);
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
