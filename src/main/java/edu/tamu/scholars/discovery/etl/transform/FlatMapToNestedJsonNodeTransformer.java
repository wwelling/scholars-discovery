package edu.tamu.scholars.discovery.etl.transform;

import static edu.tamu.scholars.discovery.index.IndexConstants.CLASS;
import static edu.tamu.scholars.discovery.index.IndexConstants.ID;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.NestedReference;

@Slf4j
public class FlatMapToNestedJsonNodeTransformer implements DataTransformer<Map<String, Object>, JsonNode> {

    private final Data data;

    private final Mapper<JsonNode> mapper;

    private final Map<String, DataField> fields;

    private final Map<String, DataField> references;

    public FlatMapToNestedJsonNodeTransformer(Data data, Mapper<JsonNode> mapper) {
        this.data = data;
        this.mapper = mapper;
        this.fields = new HashMap<>();

        // this.data.getFields()
        //         .stream()
        //         .collect(Collectors.toMap(
        //                 field -> field.getDescriptor().getName(),
        //                 Function.identity()));

        this.references = new HashMap<>();
    }

    @Override
    public void preProcess() {
        // System.out.println("\n" + this.data.getName() + "\n");
        // for (DataField field : this.data.getFields()) {
        //     DataFieldDescriptor descriptor = field.getDescriptor();
        //     // System.out.println(descriptor.getName());
        //     for (NestedReference reference : descriptor.getNestedReferences()) {
        //         // System.out.println("\t" + reference.getField());
        //         DataField referenceField = this.fields.remove(reference.getField());

        //         if (Objects.nonNull(referenceField)) {
        //             this.references.put(reference.getField(), referenceField);
        //         } else {
        //             // System.out.println("\nREFERENCE FIELD NOT FOUND: " + reference.getField() + "\n");
        //         }
        //     }
        // }

        // System.out.println("\n\n\nFIELDS: " + this.fields + "\n");
        // System.out.println("REFERENCES: " + this.references + "\n\n\n");
    }

    @Override
    public JsonNode transform(Map<String, Object> data) {
        ObjectNode individual = JsonNodeFactory.instance.objectNode();

        individual.put(ID, (String) data.remove(ID));
        individual.put(CLASS, (String) data.remove(CLASS));

        // processFields(data, individual);

        // System.out.println("\nMAPPED: " + this.data.getName() + "\n" + individual + "\n");
        // System.out.println("\nDATA REMAINING: " + data + "\n");

        return individual;
    }

    public void processFields(Map<String, Object> data, ObjectNode individual) {
        this.fields.values().forEach(field -> {
            DataFieldDescriptor descriptor = field.getDescriptor();
            Object value = data.remove(descriptor.getName());
            if (Objects.nonNull(value)) {
                if (descriptor.isNested()) {
                    processNestedField(field, value, individual);
                } else {
                    processField(field, value, individual);
                }
            } else {
                log.debug("No value found for {} {}", this.data.getName(), descriptor.getName());
            }
        });
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
