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
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.solr.common.SolrInputDocument;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.FieldDestination;
import edu.tamu.scholars.discovery.factory.index.SolrIndex;

@Slf4j
public class SolrIndexLoader implements DataLoader<SolrInputDocument> {

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
    public void load(Collection<SolrInputDocument> documents) {
        this.index.update(documents);
        log.info("{} {} documents loaded", documents.size(), this.data.getName());
    }

    @Override
    public void load(SolrInputDocument document) {
        this.index.update(document);
    }

    private void preprocessFields() {
        ObjectNode schemaRequest = objectMapper.createObjectNode();
        ArrayNode addFields = objectMapper.createArrayNode();
        ArrayNode addCopyFields = objectMapper.createArrayNode();

        processField(getDescriptor("label"), addFields, addCopyFields);
        processField(getDescriptor("class"), addFields, addCopyFields);

        this.data.getFields()
                .stream()
                .map(DataField::getDescriptor)
                .forEach(descriptor -> processFields(descriptor, addFields, addCopyFields));

        if (addFields.isEmpty()) {
            log.debug("{} index fields already exist.", this.data.getName());
        } else {
            schemaRequest.set("add-field", addFields);
        }

        if (addCopyFields.isEmpty()) {
            log.debug("{} index copy fields already exist.", this.data.getName());
        } else {
            schemaRequest.set("add-copy-field", addCopyFields);
        }

        if (!schemaRequest.isEmpty()) {
            JsonNode updateSchema = index.schema(schemaRequest);

            if (!updateSchema.isEmpty()) {
                log.debug("{} index fields exist.", this.data.getName());
            }
        }
    }

    private void processFields(DataFieldDescriptor descriptor, ArrayNode addFields, ArrayNode addCopyFields) {
        processField(descriptor, addFields, addCopyFields);
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {
            processFields(nestedDescriptor, addFields, addCopyFields);
        }
    }

    private void processField(DataFieldDescriptor descriptor, ArrayNode addFields, ArrayNode addCopyFields) {
        String name = getFieldName(descriptor);
        log.debug("Processing {} field", name);

        if (!existingFields.containsKey(name)) {
            JsonNode field = buildAddFieldNode(descriptor);
            addFields.add(field);
            existingFields.put(name, field);
        } else {
            log.debug("{} field already exists", name);
        }

        Set<String> copyTo = descriptor
                .getDestination()
                .getCopyTo();

        for (String dest : copyTo) {
            log.debug("Processing {} => {} copy field", name, dest);
            if (!existingCopyFields.containsKey(Pair.of(name, dest))) {
                JsonNode copyField = buildAddCopyFieldNode(name, dest);
                addCopyFields.add(copyField);
                existingCopyFields.put(Pair.of(name, dest), copyField);
            } else {
                log.debug("{} => {} copy field already exists", name, dest);
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
