package edu.tamu.scholars.discovery.discovery.service;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.removeEnd;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SolrService {

    @Value("${discovery.index.host:http://localhost:8983/solr}")
    private String indexHost;

    @Value("${discovery.index.name:scholars-discovery}")
    private String collectionName;

    private final RestTemplate restTemplate;

    SolrService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<JsonNode> ping() {
        String url = getUrl("admin", "ping");

        return restTemplate.getForEntity(url, JsonNode.class);
    }

    public ResponseEntity<JsonNode> addField(JsonNode addFieldNode) {
        String url = getUrlWithQuery("schema", "commit=true");

        HttpEntity<JsonNode> requestEntity = getHttpEntity(addFieldNode);

        return restTemplate.postForEntity(url, requestEntity, JsonNode.class);
    }

    public ResponseEntity<JsonNode> addCopyField(JsonNode addCopyFieldNode) {
        String url = getUrlWithQuery("schema", "commit=true");

        HttpEntity<JsonNode> requestEntity = getHttpEntity(addCopyFieldNode);

        return restTemplate.postForEntity(url, requestEntity, JsonNode.class);
    }

    public ResponseEntity<JsonNode> index(Collection<Map<String, Object>> documents) {
        String url = getUrlWithQuery("update", "commit=true");

        HttpEntity<Collection<Map<String, Object>>> requestEntity = getHttpEntity(documents);

        return restTemplate.postForEntity(url, requestEntity, JsonNode.class);
    }

    public ResponseEntity<JsonNode> index(Map<String, Object> document) {
        String url = getUrlWithQuery("update", "commit=true");

        HttpEntity<Collection<Map<String, Object>>> requestEntity = getHttpEntity(Arrays.asList(document));

        return restTemplate.postForEntity(url, requestEntity, JsonNode.class);
    }

    public ResponseEntity<JsonNode> optimize() {
        String url = getUrlWithQuery("update", "optimize=true");
        HttpEntity<String> requestEntity = getHttpEntity("");

        return restTemplate.postForEntity(url, requestEntity, JsonNode.class);
    }

    private String getUrl(String... paths) {
        String host = removeEnd(indexHost, "/");
        String path = join("/", paths);

        return join("/", host, collectionName, path);
    }

    private String getUrlWithQuery(String path, String query) {
        return format("%s?%s", getUrl(path), query);
    }

    private <S> HttpEntity<S> getHttpEntity(S requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(requestBody, headers);
    }

}
