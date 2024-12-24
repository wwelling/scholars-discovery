package edu.tamu.scholars.discovery.discovery.component.solr;

import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.ID;
import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.MOD_TIME;
import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.TYPE;
import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.tamu.scholars.discovery.discovery.argument.BoostArg;
import edu.tamu.scholars.discovery.discovery.argument.FacetArg;
import edu.tamu.scholars.discovery.discovery.argument.FilterArg;
import edu.tamu.scholars.discovery.discovery.argument.HighlightArg;
import edu.tamu.scholars.discovery.discovery.argument.QueryArg;
import edu.tamu.scholars.discovery.discovery.exception.SolrRequestException;
import edu.tamu.scholars.discovery.discovery.model.Individual;
import edu.tamu.scholars.discovery.discovery.response.DiscoveryFacetAndHighlightPage;

@Service
public class SolrService {

    @Value("${spring.data.solr.parser:edismax}")
    private String defType;

    @Value("${spring.data.solr.operator:AND}")
    private String defaultOperator;

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


    public long count(QueryArg query, List<FilterArg> filters) {
        String queryParams = SolrQueryBuilder.from(defType, defaultOperator)
            .withQuery(query)
            .withFilters(filters)
            .withRows(0)
            .build();

        String url = getUrlWithQuery("select", queryParams);

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody()
                .get("response")
                .get("numFound")
                .asLong();
        }

        return 0;
    }

    public long count(String query, List<FilterArg> filters) {
        String queryParams = SolrQueryBuilder.from(defType, defaultOperator)
            .withQuery(query)
            .withFilters(filters)
            .withRows(0)
            .build();

        String url = getUrlWithQuery("select", queryParams);

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody()
                .get("response")
                .get("numFound")
                .asLong();
        }

        return 0;
    }


    public Optional<Individual> findById(String id) {
        String queryParams = SolrQueryBuilder.from(defType, defaultOperator)
            .withQuery(String.format("%s:%s", ID, id))
            .withRows(1)
            .build();

        String url = getUrlWithQuery("select", queryParams);

        ResponseEntity<JsonNode> response =  restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Long count = response.getBody()
                .get("response")
                .get("numFound")
                .asLong();

            if (count == 1) {
                JsonNode document = response.getBody()
                    .get("response")
                    .get("docs")
                    .get(0);

                return Optional.of(Individual.from(document));
            }
        }

        return Optional.empty();
    }

    public Page<Individual> findAll(Pageable pageable) {
        String queryParams = SolrQueryBuilder.from(defType, defaultOperator)
            .withPage(pageable)
            .build();

        String url = getUrlWithQuery("select", queryParams);

        ResponseEntity<JsonNode> response =  restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {

            JsonNode responseBody = response.getBody()
                .get("response");

            long totalElements = responseBody
                .get("numFound")
                .asLong();

            ArrayNode documents = (ArrayNode) responseBody
                .get("docs");

            List<Individual> individuals = new ArrayList<>();
            for (JsonNode document : documents) {
                individuals.add(Individual.from(document));
            }

            return new PageImpl<>(individuals, pageable, totalElements);
        }

        return Page.empty();
    }

    public List<Individual> findByType(String type) {
        FilterArg filter = FilterArg.of(TYPE, Optional.of(type), Optional.empty(), Optional.empty());
        String queryParams = SolrQueryBuilder.from(defType, defaultOperator)
            .withFilters(Arrays.asList(filter))
            .withRows(Integer.MAX_VALUE)
            .build();

        String url = getUrlWithQuery("select", queryParams);

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {

            ArrayNode documents = (ArrayNode) response.getBody()
                .get("response")
                .get("docs");

            List<Individual> individuals = new ArrayList<>();
            for (JsonNode document : documents) {
                individuals.add(Individual.from(document));
            }

            return individuals;
        }

        return Arrays.asList();
    }

    public List<Individual> findByIdIn(List<String> ids, List<FilterArg> filters, Sort sort, int limit) {

        Map<String, Object> requestBody = SolrQueryBuilder.from(defType, defaultOperator)
            .withFilters(filters)
            .withSort(sort)
            .withRows(limit)
            .build(ids);

        String url = getUrl("query");

        HttpEntity<Map<String, Object>> requestEntity = getHttpEntity(requestBody);

        ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, requestEntity, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {

            ArrayNode documents = (ArrayNode) response.getBody()
                .get("response")
                .get("docs");

            List<Individual> individuals = new ArrayList<>();
            for (JsonNode document : documents) {
                individuals.add(Individual.from(document));
            }

            return individuals;
        }

        return Arrays.asList();
    }


    public List<Individual> findMostRecentlyUpdate(Integer limit, List<FilterArg> filters) {

        String queryParams = SolrQueryBuilder.from(defType, defaultOperator)
            .withFilters(filters)
            .withSort(Sort.by(Direction.DESC, MOD_TIME))
            .withRows(limit)
            .build();

        String url = getUrlWithQuery("select", queryParams);

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {

            ArrayNode documents = (ArrayNode) response.getBody()
                .get("response")
                .get("docs");

            List<Individual> individuals = new ArrayList<>();
            for (JsonNode document : documents) {
                individuals.add(Individual.from(document));
            }

            return individuals;
        }

        return Arrays.asList();
    }

    public DiscoveryFacetAndHighlightPage<Individual> search(QueryArg query, List<FacetArg> facets, List<FilterArg> filters, List<BoostArg> boosts, HighlightArg highlight, Pageable pageable) {

        String queryParams = SolrQueryBuilder.from(defType, defaultOperator)
            .withQuery(query)
            .withFacets(facets)
            .withFilters(filters)
            .withBoosts(boosts)
            .withHighlight(highlight)
            .withPage(pageable)
            .build();

        String url = getUrlWithQuery("select", queryParams);

        System.out.println("\n\n" + url + "\n\n");

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {

            JsonNode responseBody = response.getBody()
                .get("response");

            ArrayNode documents = (ArrayNode) responseBody
                .get("docs");

            List<Individual> individuals = new ArrayList<>();
            for (JsonNode document : documents) {
                individuals.add(Individual.from(document));
            }

            return DiscoveryFacetAndHighlightPage.from(
                individuals,
                responseBody,
                pageable,
                facets,
                highlight,
                Individual.class
            );

        }

        throw new SolrRequestException("Failed to search documents");
    }


    private String getUrl(String... paths) {
        String host = removeEnd(indexHost, "/");
        String path = join("/", paths);

        return join("/", host, collectionName, path);
    }

    private String getUrlWithQuery(String path, String queryParams) {
        return format("%s?%s", getUrl(path), removeStart(queryParams, "?"));
    }

    private <S> HttpEntity<S> getHttpEntity(S requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(requestBody, headers);
    }

}
