package edu.tamu.scholars.discovery.etl.load;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.FieldDestination;
import edu.tamu.scholars.discovery.factory.ManagedRestTemplate;
import edu.tamu.scholars.discovery.factory.ManagedRestTemplateFactory;

@Slf4j
public class SolrIndexLoader implements DataLoader<Map<String, Object>> {

    private final Data data;

    private final Map<String, String> properties;

    private final Set<DataField> fields;

    private final ObjectMapper objectMapper;

    private final ManagedRestTemplate restTemplate;

    private final Map<String, JsonNode> existingFields;

    private final Map<Pair<String, String>, JsonNode> existingCopyFields;

    public SolrIndexLoader(Data data) {
        this.data = data;
        this.properties = data.getLoader().getAttributes();
        this.fields = data.getFields();

        this.objectMapper = new ObjectMapper();

        this.restTemplate = ManagedRestTemplateFactory.of(this.properties)
            .withErrorHandler(new SolrResponseErrorHandler());

        this.existingFields = new HashMap<>();
        this.existingCopyFields = new HashMap<>();
    }

    @Override
    public void init() {
        this.existingFields.putAll(getFields());
        this.existingCopyFields.putAll(getCopyFields());
    }

    @Override
    public void preProcess() {
        initializeFields();
    }

    @Override
    public void destroy() {
        this.existingFields.clear();
        this.existingCopyFields.clear();
        this.restTemplate.destroy();
    }

    @Override
    public void load(Collection<Map<String, Object>> documents) {
        // TODO
    }

    @Override
    public void load(Map<String, Object> document) {
        // TODO
    }

    private Map<String, JsonNode> getFields() {
        String url = getUrl("schema", "fields");

        ResponseEntity<JsonNode> response = this.restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Iterable<JsonNode> iterable = () -> response.getBody().get("fields").iterator();
            Stream<JsonNode> stream = StreamSupport.stream(iterable.spliterator(), false);

            return stream.collect(Collectors.toMap(node -> node.get("name").asText(), node -> node));
        }

        return Map.of();
    }

    private Map<Pair<String, String>, JsonNode> getCopyFields() {
        String url = getUrl("schema", "copyfields");

        ResponseEntity<JsonNode> response = this.restTemplate.getForEntity(url, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Iterable<JsonNode> iterable = () -> response.getBody().get("copyFields").iterator();
            Stream<JsonNode> stream = StreamSupport.stream(iterable.spliterator(), false);

            return stream.collect(Collectors.toMap(
                node -> Pair.of(node.get("source").asText(), node.get("dest").asText()),
                node -> node));
        }

        return Map.of();
    }

    private void initializeFields() {
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
        } else {
            log.info("{} index fields already initialized.", this.data.getName());
        }

        if (!addCopyFieldNodes.isEmpty()) {
            schemaRequestNode.set("add-copy-field", addCopyFieldNodes);
        } else {
            log.info("{} index copy fields already initialized.", this.data.getName());
        }

        if (!schemaRequestNode.isEmpty()) {
            ResponseEntity<JsonNode> updateSchemaRepsonse = updateSchema(schemaRequestNode);

            if (updateSchemaRepsonse.getStatusCode().isError()) {
                // TODO: log error
            } else {
                log.info("{} index fields initialized.", this.data.getName());
            }
        }

    }

    private void processField(DataFieldDescriptor descriptor, ArrayNode addFieldNodes, ArrayNode addCopyFieldNodes) {
        String name = descriptor.getName();

        if (!existingFields.containsKey(name)) {
            addFieldNodes.add(buildAddFieldNode(descriptor));
        }

        Set<String> copyTo = descriptor
            .getDestination()
            .getCopyTo();

        for (String dest : copyTo) {
            if (!existingCopyFields.containsKey(Pair.of(name, dest))) {
                addCopyFieldNodes.add(buildAddCopyFieldNode(name, dest));
            }
        }
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

        return this.restTemplate.postForEntity(url, requestEntity, JsonNode.class);
    }

    private String getUrl(String... paths) {
        String host = properties.getOrDefault("host", "http://localhost:8983/solr");

        String baseUrl = removeEnd(host, "/");

        String collection = properties.getOrDefault("collection", "scholars-discovery");

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
