package edu.tamu.scholars.discovery.etl.load;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.FieldDestination;

public class SolrIndexLoader implements DataLoader<Map<String, Object>> {

    private final Map<String, String> properties;

    private final ObjectMapper objectMapper;

    private final PoolingHttpClientConnectionManager connectionManager;

    private final CloseableHttpClient httpClient;

    private final HttpComponentsClientHttpRequestFactory requestFactory;

    private final RestTemplate restTemplate;

    private final Map<String, JsonNode> existingFields;

    private final Map<Pair<String, String>, JsonNode> existingCopyFields;

    public SolrIndexLoader(Map<String, String> properties) {
        this.properties = properties;
        this.objectMapper = new ObjectMapper();

        this.connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(50)
                .setMaxConnPerRoute(20)
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setTimeToLive(TimeValue.ofMinutes(5))
                        .build())
                .build();

        this.httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                .evictIdleConnections(TimeValue.ofMinutes(2))
                .build();

        this.requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.requestFactory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        this.requestFactory.setReadTimeout((int) Duration.ofSeconds(120).toMillis());

        this.restTemplate = new RestTemplate(this.requestFactory);

        this.restTemplate.setErrorHandler(new SolrResponseErrorHandler());

        this.existingFields = new HashMap<>();
        this.existingCopyFields = new HashMap<>();
    }

    @Override
    public void init() {
        this.existingFields.putAll(getFields());
        this.existingCopyFields.putAll(getCopyFields());
    }

    @Override
    public void preProcess(List<DataField> fields) {
        initializeFields(fields);
    }

    @Override
    public void postProcess(List<DataField> fields) {

    }

    @Override
    public void destroy() {
        this.existingFields.clear();
        this.existingCopyFields.clear();

        try {
            this.connectionManager.close();
            this.httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.requestFactory.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(Collection<Map<String, Object>> documents) {

    }

    @Override
    public void load(Map<String, Object> document) {

    }

    private void initializeFields(List<DataField> fields) {
        ObjectNode schemaRequestNode = objectMapper.createObjectNode();
        ArrayNode addFieldNodes = objectMapper.createArrayNode();
        ArrayNode addCopyFieldNodes = objectMapper.createArrayNode();

        for (DataField field : fields) {
            processField(field.getDescriptor(), addFieldNodes, addCopyFieldNodes);

            for (DataFieldDescriptor descriptor : field.getNestedDescriptors()) {
                processField(descriptor, addFieldNodes, addCopyFieldNodes);
            }
        }

        if (!addFieldNodes.isEmpty()) {
            schemaRequestNode.set("add-field", addFieldNodes);
        }

        if (!addCopyFieldNodes.isEmpty()) {
            schemaRequestNode.set("add-copy-field", addCopyFieldNodes);
        }

        if (!schemaRequestNode.isEmpty()) {
            ResponseEntity<JsonNode> updateSchemaRepsonse = updateSchema(schemaRequestNode);

            if (updateSchemaRepsonse.getStatusCode().isError()) {
                // TODO: log error
            }
        }
    }

    private void processField(DataFieldDescriptor descriptor, ArrayNode addFieldNodes, ArrayNode addCopyFieldNodes) {
        String name = descriptor.getName();

        if (!existingFields.containsKey(name)) {
            addFieldNodes.add(buildAddFieldNode(descriptor));
        }

        List<String> copyTo = descriptor
                .getDestination()
                .getCopyTo();

        for (String dest : copyTo) {
            if (!existingCopyFields.containsKey(Pair.of(name, dest))) {
                addCopyFieldNodes.add(buildAddCopyFieldNode(name, dest));
            }
        }
    }

    private Map<String, JsonNode> getFields() {
        String url = getUrl("schema", "fields");

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Iterable<JsonNode> iterable = () -> response.getBody().get("fields").iterator();
            Stream<JsonNode> stream = StreamSupport.stream(iterable.spliterator(), false);

            return stream.collect(Collectors.toMap(
                    node -> node.get("name").asText(),
                    node -> node));
        }

        return Map.of();
    }

    private Map<Pair<String, String>, JsonNode> getCopyFields() {
        String url = getUrl("schema", "copyfields");

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Iterable<JsonNode> iterable = () -> response.getBody().get("copyFields").iterator();
            Stream<JsonNode> stream = StreamSupport.stream(iterable.spliterator(), false);

            return stream.collect(Collectors.toMap(
                    node -> Pair.of(node.get("source").asText(), node.get("dest").asText()),
                    node -> node));
        }

        return Map.of();
    }

    private JsonNode buildAddFieldNode(DataFieldDescriptor descriptor) {
        FieldDestination destination = descriptor.getDestination();

        ObjectNode addFieldNode = objectMapper.createObjectNode();
        addFieldNode.put("name", descriptor.getName());
        addFieldNode.put("type", destination.getType());
        addFieldNode.put("required", destination.isRequired());
        addFieldNode.put("stored", destination.isStored());
        addFieldNode.put("indexed", destination.isIndexed());
        addFieldNode.put("docValues", destination.isDocValues());

        addFieldNode.put("multiValued", destination.isMultiValued());

        if (StringUtils.isNotEmpty(destination.getDefaultValue())) {
            addFieldNode.put("defaultValue", destination.getDefaultValue());
        }

        return addFieldNode;
    }

    private JsonNode buildAddCopyFieldNode(String source, String destination) {
        ObjectNode addCopyFieldNode = objectMapper.createObjectNode();
        addCopyFieldNode.put("source", source);

        ArrayNode destinationFields = objectMapper.createArrayNode();

        destinationFields.add(destination);

        addCopyFieldNode.set("dest", destinationFields);

        return addCopyFieldNode;
    }

    private ResponseEntity<JsonNode> updateSchema(JsonNode schemaRequestNode) {
        String url = getUrlWithQuery("schema", "commit=true");

        HttpEntity<JsonNode> requestEntity = getHttpEntity(schemaRequestNode);

        return restTemplate.postForEntity(url, requestEntity, JsonNode.class);
    }

    private String getUrl(String... paths) {
        String host = properties.containsKey("host")
                ? properties.get("host")
                : "http://localhost:8983/solr";

        String baseUrl = removeEnd(host, "/");

        String collection = properties.containsKey("collection")
                ? properties.get("collection")
                : "scholars-discovery";

        String path = join("/", paths);

        return join("/", baseUrl, collection, path);
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

}
