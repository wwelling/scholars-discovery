package edu.tamu.scholars.discovery.etl.load;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.FieldDestination;
import edu.tamu.scholars.discovery.factory.index.SolrIndex;

@Slf4j
public class SolrIndexLoader implements DataLoader<JsonNode> {

    private final Data data;

    private final SolrIndex index;

    private final ObjectMapper objectMapper;

    private final Map<String, JsonNode> existingFields;

    private final Map<Pair<String, String>, JsonNode> existingCopyFields;

    public SolrIndexLoader(Data data) {
        this.data = data;

        this.index = SolrIndex.of(data.getLoader().getAttributes());

        this.objectMapper = new ObjectMapper();

        this.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));

        this.existingFields = new HashMap<>();
        this.existingCopyFields = new HashMap<>();
    }

    @Override
    public void init() {
        Map<String, JsonNode> fields = this.index.fields()
            .collect(Collectors.toMap(node -> node.get("name").asText(), node -> node));

        this.existingFields.putAll(fields);

        Map<Pair<String, String>, JsonNode> copyFields = this.index.copyFields()
            .collect(Collectors.toMap(
                node -> Pair.of(node.get("source").asText(), node.get("dest").asText()),
                node -> node));

        this.existingCopyFields.putAll(copyFields);
    }

    @Override
    public void preprocess() {
        preprocessFields();
    }

    @Override
    public void destroy() {
        this.index.close();
    }

    @Override
    public void load(Collection<JsonNode> documents) {
        log.info("Loading {} {} documents", documents.size(), this.data.getName());

        ObjectNode update = JsonNodeFactory.instance.objectNode();
        ArrayNode add = documents.parallelStream()
            .map(this::wrapDocument)
            .collect(JsonNodeFactory.instance::arrayNode, ArrayNode::add, ArrayNode::addAll);

        update.set("add", add);

        this.index.update(update);
    }

    @Override
    public void load(JsonNode document) {
        log.info("Loading {} document", this.data.getName());

        ObjectNode update = JsonNodeFactory.instance.objectNode();
        ObjectNode add = JsonNodeFactory.instance.objectNode();

        add.set("doc", document);
        update.set("add", add);

        this.index.update(update);
    }

    private JsonNode wrapDocument(JsonNode document) {
        ObjectNode doc = JsonNodeFactory.instance.objectNode();
        doc.set("doc", document);

        return doc;
    }

    private void preprocessFields() {
        ObjectNode schemaRequestNode = objectMapper.createObjectNode();
        ArrayNode addFieldNodes = objectMapper.createArrayNode();
        ArrayNode addCopyFieldNodes = objectMapper.createArrayNode();

        processField(getDescriptor("label"), addFieldNodes, addCopyFieldNodes);
        processField(getDescriptor("class"), addFieldNodes, addCopyFieldNodes);

        this.data.getFields()
            .stream()
            .map(DataField::getDescriptor)
            .forEach(descriptor -> processFields(descriptor, addFieldNodes, addCopyFieldNodes));

        if (addFieldNodes.isEmpty()) {
            log.info("{} index fields already preprocessed.", this.data.getName());
        } else {
            schemaRequestNode.set("add-field", addFieldNodes);
        }

        if (addCopyFieldNodes.isEmpty()) {
            log.info("{} index copy fields already preprocessed.", this.data.getName());
        } else {
            schemaRequestNode.set("add-copy-field", addCopyFieldNodes);
        }

        if (!schemaRequestNode.isEmpty()) {
            ResponseEntity<JsonNode> updateSchemaRepsonse = index.schema(schemaRequestNode);

            if (updateSchemaRepsonse.getStatusCode().is2xxSuccessful()) {
                log.info("{} index fields preprocessed.", this.data.getName());
            }
        }
    }

    private void processFields(DataFieldDescriptor descriptor, ArrayNode addFieldNodes, ArrayNode addCopyFieldNodes) {
        processField(descriptor, addFieldNodes, addCopyFieldNodes);
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {
            processFields(nestedDescriptor, addFieldNodes, addCopyFieldNodes);
        }
    }

    private void processField(DataFieldDescriptor descriptor, ArrayNode addFieldNodes, ArrayNode addCopyFieldNodes) {
        String name = getFieldName(descriptor);

        if (!existingFields.containsKey(name)) {
            JsonNode field = buildAddFieldNode(descriptor);
            addFieldNodes.add(field);
            existingFields.put(name, field);
        }

        Set<String> copyTo = descriptor
            .getDestination()
            .getCopyTo();

        for (String dest : copyTo) {
            if (!existingCopyFields.containsKey(Pair.of(name, dest))) {
                JsonNode copyField = buildAddCopyFieldNode(name, dest);
                addCopyFieldNodes.add(copyField);
                existingCopyFields.put(Pair.of(name, dest), copyField);
            }
        }
    }

    private JsonNode buildAddFieldNode(DataFieldDescriptor descriptor) {
        FieldDestination destination = descriptor.getDestination();
        String name = getFieldName(descriptor);

        ObjectNode addFieldNode = objectMapper.createObjectNode();
        addFieldNode.put("name", name);
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

    private String getFieldName(DataFieldDescriptor descriptor) {
        return Objects.nonNull(descriptor.getNestedReference())
            && StringUtils.isNotEmpty(descriptor.getNestedReference().getKey())
                ? descriptor.getNestedReference().getKey()
                : descriptor.getName();
    }

    private DataFieldDescriptor getDescriptor(String name) {
        DataFieldDescriptor descriptor = new DataFieldDescriptor();
        FieldDestination destination = new FieldDestination();

        descriptor.setName(name);
        descriptor.setDestination(destination);

        return descriptor;
    }

}
