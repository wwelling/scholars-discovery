package edu.tamu.scholars.discovery.etl.transform;

import static edu.tamu.scholars.discovery.index.IndexConstants.CLASS;
import static edu.tamu.scholars.discovery.index.IndexConstants.ID;
import static edu.tamu.scholars.discovery.index.IndexConstants.LABEL;
import static edu.tamu.scholars.discovery.index.IndexConstants.NESTED_DELIMITER;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

        this.fields = this.data.getFields()
                .stream()
                .collect(Collectors.toMap(
                        field -> field.getDescriptor().getName(),
                        Function.identity()));

        this.references = new HashMap<>();
    }

    @Override
    public void preProcess() {
        // duplicate in IndexLoader
        for (DataField field : this.data.getFields()) {
            DataFieldDescriptor descriptor = field.getDescriptor();
            for (NestedReference reference : descriptor.getNestedReferences()) {
                DataField referenceField = this.fields.remove(reference.getField());
                if (Objects.nonNull(referenceField)) {
                    this.references.put(reference.getField(), referenceField);
                }
            }
        }
    }

    @Override
    public JsonNode transform(Map<String, Object> data) {
        ObjectNode individual = JsonNodeFactory.instance.objectNode();

        individual.put(ID, (String) data.remove(ID));
        individual.put(CLASS, (String) data.remove(CLASS));

        processFields(data, individual);

        return individual;
    }

    private void processFields(Map<String, Object> data, ObjectNode individual) {
        this.fields.values().forEach(field -> {
            DataFieldDescriptor descriptor = field.getDescriptor();
            if (descriptor.isNested()) {
                processNestedField(data, field, individual);
            } else {
                processField(data, field, individual);
            }
        });
    }

    private void processField(Map<String, Object> data, DataField field, ObjectNode individual) {
        DataFieldDescriptor descriptor = field.getDescriptor();
        Object object = data.remove(descriptor.getName());
        if (Objects.nonNull(object)) {
            if (descriptor.getDestination().isMultiValued()) {
                individual.set(descriptor.getName(), mapper.valueToTree(object));
            } else {
                individual.put(descriptor.getName(), object.toString());
            }
        }
    }

    private void processNestedField(Map<String, Object> data, DataField field, ObjectNode individual) {
        DataFieldDescriptor descriptor = field.getDescriptor();
        String name = descriptor.getName();

        Object object = data.remove(name);

        if (Objects.nonNull(object)) {
            if (descriptor.getDestination().isMultiValued()) {
                @SuppressWarnings("unchecked")
                List<String> values = (List<String>) object;
                for (String value : values) {
                    String[] parts = value.split(NESTED_DELIMITER);

                    ArrayNode nodes = JsonNodeFactory.instance.arrayNode();
                    ObjectNode node = processNestedValue(data, field, parts, 1);

                    nodes.add(node);

                    individual.set(name, nodes);
                }
            } else {
                String value = object.toString();
                String[] parts = value.split(NESTED_DELIMITER);

                ObjectNode node = processNestedValue(data, field, parts, 1);

                individual.set(name, node);
            }
        }
    }

    private ObjectNode processNestedValue(Map<String, Object> data, DataField field, String[] parts, int index) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        node.put(ID, parts[index]);
        node.put(LABEL, parts[0]);

        if (field.getDescriptor().isNested()) {
            processNestedReferences(data, field, node, parts, index + 1);
        }

        return node;
    }

    public void processNestedReferences(Map<String, Object> data, DataField field, ObjectNode node, String[] parts, int depth) {
        for (NestedReference nestedReference : field.getDescriptor().getNestedReferences()) {

            String name = nestedReference.getKey();

            DataField nestedField = this.references.get(nestedReference.getField());

            DataFieldDescriptor nestedDescriptor = nestedField.getDescriptor();

            Object nestedObject = data.remove(nestedDescriptor.getName());

            if (Objects.nonNull(nestedObject)) {

                if (nestedDescriptor.getDestination().isMultiValued()) {

                    @SuppressWarnings("unchecked")
                    List<String> nestedValues = (List<String>) nestedObject;

                    if (nestedValues.size() > 0) {
                        ArrayNode array;

                        if (nestedValues.get(0).split(NESTED_DELIMITER).length > depth) {

                            array = nestedValues.parallelStream()
                                .filter(nv -> isProperty(parts, nv))
                                .map(nv -> processNestedValue(data, nestedField, nv.split(NESTED_DELIMITER), depth))
                                .collect(new JsonNodeArrayNodeCollector());

                        } else {
                            array = nestedValues.parallelStream()
                                .filter(nv -> isProperty(parts, nv))
                                .map(nv -> nv.split(NESTED_DELIMITER)[0])
                                .collect(new StringArrayNodeCollector());
                        }

                        if (array.size() > 0) {
                            if (nestedReference.isMultiValued()) {
                                node.set(name, array);
                            } else {
                                node.set(name, array.get(0));
                            }
                        }
                    }

                } else {
                    String[] nestedParts = nestedObject.toString().split(NESTED_DELIMITER);

                    if (nestedParts.length > depth) {
                        node.set(name, processNestedValue(data, nestedField, nestedParts, depth));
                    } else {
                        if (nestedParts[0] != null) {
                            node.put(name, nestedParts[0]);
                        }
                    }
                }
            }
        }
    }

    private boolean isProperty(String[] parts, String value) {
        for (int i = parts.length - 1; i > 0; i--) {
            if (!value.contains(parts[i])) {
                return false;
            }
        }
        return true;
    }

    private class JsonNodeArrayNodeCollector implements Collector<JsonNode, ArrayNode, ArrayNode> {

        @Override
        public Supplier<ArrayNode> supplier() {
            return JsonNodeFactory.instance::arrayNode;
        }

        @Override
        public BiConsumer<ArrayNode, JsonNode> accumulator() {
            return ArrayNode::add;
        }

        @Override
        public BinaryOperator<ArrayNode> combiner() {
            return (x, y) -> {
                x.addAll(y);
                return x;
            };
        }

        @Override
        public Function<ArrayNode, ArrayNode> finisher() {
            return accumulator -> accumulator;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.UNORDERED);
        }

    }

    private class StringArrayNodeCollector implements Collector<String, ArrayNode, ArrayNode> {

        @Override
        public Supplier<ArrayNode> supplier() {
            return JsonNodeFactory.instance::arrayNode;
        }

        @Override
        public BiConsumer<ArrayNode, String> accumulator() {
            return ArrayNode::add;
        }

        @Override
        public BinaryOperator<ArrayNode> combiner() {
            return (x, y) -> {
                x.addAll(y);
                return x;
            };
        }

        @Override
        public Function<ArrayNode, ArrayNode> finisher() {
            return accumulator -> accumulator;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.UNORDERED);
        }

    }

}
