package edu.tamu.scholars.discovery.etl.load;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
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

    private final Map<String, JsonNode> existingFieldTypes;

    private final Map<String, JsonNode> existingFields;

    private final Map<Pair<String, String>, JsonNode> existingCopyFields;

    public SolrIndexLoader(Data data) {
        this.data = data;

        this.index = SolrIndex.of(data.getLoader().getAttributes());

        this.objectMapper = new ObjectMapper();

        this.existingFieldTypes = new HashMap<>();
        this.existingFields = new HashMap<>();
        this.existingCopyFields = new HashMap<>();
    }

    @Override
    public void init() {
        Map<String, JsonNode> fieldTypes = this.index.fields()
            .collect(Collectors.toMap(node -> node.get("name").asText(), node -> node));

        this.existingFieldTypes.putAll(fieldTypes);

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
        log.info("{} {} documents loaded.", documents.size(), this.data.getName());
    }

    @Override
    public void load(SolrInputDocument document) {
        this.index.update(document);
    }

    private void preprocessFields() {
        log.info("Preprocessing fields for {} documents.", this.data.getName());
        ObjectNode schema = objectMapper.createObjectNode();

        ArrayNode addFieldTypes = objectMapper.createArrayNode();
        ArrayNode replaceFields = objectMapper.createArrayNode();
        ArrayNode addFields = objectMapper.createArrayNode();
        ArrayNode addCopyFields = objectMapper.createArrayNode();

        processField(getDescriptor("label"), addFields, addCopyFields);
        processField(getDescriptor("class"), addFields, addCopyFields);

        this.data.getFields()
            .stream()
            .map(DataField::getDescriptor)
            .forEach(descriptor -> processFields(descriptor, addFields, addCopyFields, 1));

        if (addFieldTypes.isEmpty()) {
            log.debug("{} index field types already exist.", this.data.getName());
        } else {
            schema.set("add-field-type", addFieldTypes);
        }

        if (replaceFields.isEmpty()) {
            log.debug("{} index field does not exist or has already been replaced.", this.data.getName());
        } else {
            schema.set("replace-field", replaceFields);
        }

        if (addFields.isEmpty()) {
            log.debug("{} index fields already exist.", this.data.getName());
        } else {
            schema.set("add-field", addFields);
        }

        if (addCopyFields.isEmpty()) {
            log.debug("{} index copy fields already exist.", this.data.getName());
        } else {
            schema.set("add-copy-field", addCopyFields);
        }

        if (!schema.isEmpty()) {
            JsonNode updateSchema = index.schema(schema);

            if (!updateSchema.isEmpty()) {
                log.debug("{} index fields exist.", this.data.getName());
            }
        }
    }

    private void processFields(DataFieldDescriptor descriptor, ArrayNode addFields, ArrayNode addCopyFields, int depth) {
        if (descriptor.getNestedDescriptors().isEmpty() || (descriptor.isNested() && depth > 1)) {
            processField(descriptor, addFields, addCopyFields);
        }
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {
            processFields(nestedDescriptor, addFields, addCopyFields, depth + 1);
        }
    }

    private void processField(DataFieldDescriptor descriptor, ArrayNode addFields, ArrayNode addCopyFields) {
        String name = getFieldName(descriptor);
        log.debug("Processing {} field", name);

        if (!existingFields.containsKey(name)) {
            JsonNode field = buildAddField(descriptor);
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
                JsonNode copyField = buildAddCopyField(name, dest);
                addCopyFields.add(copyField);
                existingCopyFields.put(Pair.of(name, dest), copyField);
            } else {
                log.debug("{} => {} copy field already exists", name, dest);
            }
        }
    }

    private JsonNode buildAddField(DataFieldDescriptor descriptor) {
        FieldDestination destination = descriptor.getDestination();
        String name = getFieldName(descriptor);

        ObjectNode addField = objectMapper.createObjectNode();
        addField.set("name", TextNode.valueOf(name));
        addField.set("type", TextNode.valueOf(destination.getType()));
        addField.set("required", BooleanNode.valueOf(destination.isRequired()));
        addField.set("stored", BooleanNode.valueOf(destination.isStored()));
        addField.set("indexed", BooleanNode.valueOf(destination.isIndexed()));
        addField.set("docValues", BooleanNode.valueOf(destination.isDocValues()));
        addField.set("multiValued", BooleanNode.valueOf(destination.isMultiValued()));

        if (isNotEmpty(destination.getDefaultValue())) {
            addField.set("defaultValue", TextNode.valueOf(destination.getDefaultValue()));
        }

        return addField;
    }

    private JsonNode buildAddCopyField(String source, String destination) {
        ObjectNode addCopyField = objectMapper.createObjectNode();
        addCopyField.put("source", source);

        ArrayNode destinationFields = objectMapper.createArrayNode();
        destinationFields.add(destination);

        addCopyField.set("dest", destinationFields);

        return addCopyField;
    }

    private String getFieldName(DataFieldDescriptor descriptor) {
        return isNestedReference(descriptor)
            ? descriptor.getNestedReference().getKey()
            : descriptor.getName();
    }

    private boolean isNestedReference(DataFieldDescriptor descriptor) {
        return descriptor.getNestedReference() != null
            && isNotEmpty(descriptor.getNestedReference().getKey());
    }

    private DataFieldDescriptor getDescriptor(String name) {
        DataFieldDescriptor descriptor = new DataFieldDescriptor();
        FieldDestination destination = new FieldDestination();

        descriptor.setName(name);
        descriptor.setDestination(destination);

        return descriptor;
    }

}
