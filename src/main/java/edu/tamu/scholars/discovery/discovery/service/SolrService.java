package edu.tamu.scholars.discovery.discovery.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    public void addField(JsonNode addFieldNode) {
        String url = StringUtils.removeEnd(indexHost, "/") + "/" + collectionName + "/schema";

        String requestBody = addFieldNode.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        restTemplate.postForEntity(url, requestEntity, String.class);
    }

    public void addCopyField(JsonNode addCopyFieldNode) {
        String url = StringUtils.removeEnd(indexHost, "/") + "/" + collectionName + "/schema?commit=true";

        String requestBody = addCopyFieldNode.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        restTemplate.postForEntity(url, requestEntity, String.class);
    }

    public void index(Collection<Map<String, Object>> documents) {
        String url = StringUtils.removeEnd(indexHost, "/") + "/" + collectionName + "/update?commit=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Collection<Map<String, Object>>> requestEntity = new HttpEntity<>(documents, headers);

        restTemplate.postForEntity(url, requestEntity, String.class);
    }

    public void index(Map<String, Object> document) {
        String url = StringUtils.removeEnd(indexHost, "/") + "/" + collectionName + "/update?commit=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Collection<Map<String, Object>>> requestEntity = new HttpEntity<>(Arrays.asList(document), headers);

        restTemplate.postForEntity(url, requestEntity, String.class);
    }

    public void optimize() {
        String url = StringUtils.removeEnd(indexHost, "/") + "/" + collectionName + "/update?optimize=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

        restTemplate.postForEntity(url, requestEntity, String.class);
    }

}
