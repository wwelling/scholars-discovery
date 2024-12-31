package edu.tamu.scholars.discovery.etl.transform;

import static edu.tamu.scholars.discovery.index.IndexConstants.CLASS;
import static edu.tamu.scholars.discovery.index.IndexConstants.ID;
import static edu.tamu.scholars.discovery.index.IndexConstants.LABEL;
import static edu.tamu.scholars.discovery.index.IndexConstants.NESTED_DELIMITER;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;

@Slf4j
public class FlatMapToNestedJsonNodeTransformer implements DataTransformer<Map<String, Object>, JsonNode> {

    private final Data data;

    private final ObjectMapper mapper;

    public FlatMapToNestedJsonNodeTransformer(Data data) {
        this.data = data;
        this.mapper = new ObjectMapper();
    }

    @Override
    public JsonNode transform(Map<String, Object> data) {
        ObjectNode individual = JsonNodeFactory.instance.objectNode();

        individual.put(ID, (String) data.remove(ID));
        individual.put(CLASS, (String) data.remove(CLASS));

        processFields(data, individual);

        // System.out.println("\n" + this.data.getName() + "\n" + individual + "\n");

        return individual;
    }

    private void processFields(Map<String, Object> data, ObjectNode individual) {
        this.data.getFields()
            .parallelStream()
            .map(DataField::getDescriptor)
            .forEach(descriptor -> processField(data, descriptor, individual));
    }

    private void processField(Map<String, Object> data, DataFieldDescriptor descriptor, ObjectNode individual) {
        Object object = data.remove(descriptor.getName());
        if (Objects.nonNull(object)) {
            String name = descriptor.getName();
            if (descriptor.isNested()) {
                if (descriptor.getDestination().isMultiValued()) {
                    @SuppressWarnings("unchecked")
                    List<String> values = (List<String>) object;
                    ArrayNode array = values.parallelStream()
                        .map(value -> value.split(NESTED_DELIMITER))
                        .filter(parts -> parts.length > 1)
                        .map(parts -> processNestedValue(data, descriptor, parts, 1))
                        .collect(new JsonNodeArrayNodeCollector());
                    if (array.size() > 0) {
                        individual.set(name, array);
                    }
                } else {
                    String[] parts = object.toString().split(NESTED_DELIMITER);
                    if (parts.length > 1) {
                        individual.set(name, processNestedValue(data, descriptor, parts, 1));
                    }
                }
            } else {
                if (descriptor.getDestination().isMultiValued()) {
                    individual.set(name, mapper.valueToTree(object));
                } else {
                    individual.put(name, object.toString());
                }
            }
        }
    }

    private ObjectNode processNestedValue(Map<String, Object> data, DataFieldDescriptor descriptor, String[] parts, int index) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        node.put(ID, parts[index]);
        node.put(LABEL, parts[0]);

        processNestedReferences(data, descriptor, node, parts, index + 1);

        return node;
    }

    public void processNestedReferences(Map<String, Object> data, DataFieldDescriptor descriptor, ObjectNode node, String[] parts, int depth) {
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {

            String name = nestedDescriptor.getNestedReference().getKey();

            Object nestedObject = data.remove(nestedDescriptor.getName());

            if (Objects.nonNull(nestedObject)) {

                if (nestedDescriptor.getDestination().isMultiValued()) {

                    @SuppressWarnings("unchecked")
                    List<String> nestedValues = (List<String>) nestedObject;

                    if (!nestedValues.isEmpty()) {
                        ArrayNode array;

                        if (nestedValues.get(0).split(NESTED_DELIMITER).length > depth) {

                            array = nestedValues.parallelStream()
                                .filter(nv -> isProperty(parts, nv))
                                .map(nv -> processNestedValue(data, nestedDescriptor, nv.split(NESTED_DELIMITER), depth))
                                .collect(new JsonNodeArrayNodeCollector());

                        } else {
                            array = nestedValues.parallelStream()
                                .filter(nv -> isProperty(parts, nv))
                                .map(nv -> nv.split(NESTED_DELIMITER)[0])
                                .collect(new StringArrayNodeCollector());
                        }

                        if (array.size() > 0) {

                            boolean isMultiple = Objects.nonNull(nestedDescriptor.getNestedReference())
                                && Objects.nonNull(nestedDescriptor.getNestedReference().getMultiple())
                                && nestedDescriptor.getNestedReference().getMultiple();

                            if (isMultiple) {
                                node.set(name, array);
                            } else {
                                node.set(name, array.get(0));
                            }
                        }
                    }

                } else {
                    String[] nestedParts = nestedObject.toString().split(NESTED_DELIMITER);

                    if (nestedParts.length > depth) {
                        node.set(name, processNestedValue(data, nestedDescriptor, nestedParts, depth));
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
