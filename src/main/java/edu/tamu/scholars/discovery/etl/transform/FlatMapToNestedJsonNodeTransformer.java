package edu.tamu.scholars.discovery.etl.transform;

import static edu.tamu.scholars.discovery.index.IndexConstants.CLASS;
import static edu.tamu.scholars.discovery.index.IndexConstants.ID;

import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jsonldjava.shaded.com.google.common.base.Functions;
import lombok.extern.slf4j.Slf4j;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;

@Slf4j
public class FlatMapToNestedJsonNodeTransformer implements DataTransformer<Map<String, Object>, JsonNode> {

    private final Data data;

    private final ObjectMapper mapper;

    private final Map<String, DataField> fields;

    public FlatMapToNestedJsonNodeTransformer(Data data) {
        this.data = data;
        this.mapper = new ObjectMapper();
        this.fields = this.data.getFields()
            .stream()
            .collect(Collectors.toMap(
                field -> field.getDescriptor().getName(),
                Functions.identity()));
    }

    @Override
    public JsonNode transform(Map<String, Object> data) {
        ObjectNode individual = JsonNodeFactory.instance.objectNode();

        individual.put(ID, (String) data.get(ID));
        individual.put(CLASS, (String) data.get(CLASS));

        processFields(data, individual);

        System.out.println("\n" + this.data.getName() + "\n" + individual + "\n");

        return individual;
    }

    public void processFields(Map<String, Object> data, ObjectNode individual) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();

            DataField field = fields.remove(fieldName);
            DataFieldDescriptor descriptor = field.getDescriptor();

            if (descriptor.isNested()) {
                processNestedField(field, value, individual);
            } else {
                processField(field, value, individual);
            }
        }
    }

    public void processNestedField(DataField field, Object value, ObjectNode individual) {

    }

    public void processField(DataField field, Object value, ObjectNode individual) {
        DataFieldDescriptor descriptor = field.getDescriptor();
        if (descriptor.getDestination().isMultiValued()) {
            individual.set(descriptor.getName(), mapper.valueToTree(value));
        } else {
            individual.put(descriptor.getName(), value.toString());
        }
    }

}
