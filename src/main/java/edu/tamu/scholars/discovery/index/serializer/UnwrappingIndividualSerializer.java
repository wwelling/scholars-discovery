package edu.tamu.scholars.discovery.index.serializer;

import static edu.tamu.scholars.discovery.index.DiscoveryConstants.CLASS;
import static edu.tamu.scholars.discovery.index.DiscoveryConstants.ID;
// import static edu.tamu.scholars.discovery.index.DiscoveryConstants.NESTED_DELIMITER;
import static edu.tamu.scholars.discovery.utility.DiscoveryUtility.getDiscoveryDocumentType;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.NameTransformer;
import org.apache.commons.lang3.reflect.FieldUtils;

import edu.tamu.scholars.discovery.index.annotation.FieldSource;
import edu.tamu.scholars.discovery.index.annotation.NestedMultiValuedProperty;
import edu.tamu.scholars.discovery.index.annotation.NestedObject;
import edu.tamu.scholars.discovery.index.annotation.NestedObject.Reference;
import edu.tamu.scholars.discovery.index.model.Individual;

public class UnwrappingIndividualSerializer extends JsonSerializer<Individual> {

    // private static final ObjectMapper mapper = new ObjectMapper();

    private final NameTransformer nameTransformer;

    public UnwrappingIndividualSerializer(final NameTransformer nameTransformer) {
        this.nameTransformer = nameTransformer;
    }

    @Override
    public boolean isUnwrappingSerializer() {
        return true;
    }

    @Override
    public void serialize(
        Individual individual,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException, JsonProcessingException {
        Class<?> type = getDiscoveryDocumentType(individual.getProxy());
        Map<String, Object> content = individual.getContent();
        jsonGenerator.writeObjectField(nameTransformer.transform(ID), individual.getId());
        jsonGenerator.writeObjectField(nameTransformer.transform(CLASS), individual.getProxy());
        for (Field field : FieldUtils.getFieldsListWithAnnotation(type, FieldSource.class)) {
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            String name = nameTransformer.transform(jsonProperty != null ? jsonProperty.value() : field.getName());
            Object value = content.get(name);
            if (value != null) {
                // NestedObject nestedObject = field.getAnnotation(NestedObject.class);
                // if (nestedObject != null) {
                //     if (nestedObject.root()) {
                //         if (List.class.isAssignableFrom(field.getType())) {

                //             @SuppressWarnings("unchecked")
                //             List<String> values = (List<String>) value;

                //             ArrayNode array = values.parallelStream()
                //                 .map(v -> v.split(NESTED_DELIMITER))
                //                 .filter(vparts -> vparts.length > 1)
                //                 .map(vparts -> processValue(content, type, field, vparts, 1))
                //                 .collect(new JsonNodeArrayNodeCollector());

                //             if (array.size() > 0) {
                //                 jsonGenerator.writeObjectField(name, array);
                //             }
                //         } else {
                //             String[] vparts = strip(value.toString()).split(NESTED_DELIMITER);
                //             if (vparts.length > 1) {
                //                 jsonGenerator.writeObjectField(name, processValue(content, type, field, vparts, 1));
                //             }
                //         }
                //     }
                // } else {
                    // if (!value.toString().contains(NESTED_DELIMITER)) {
                        if (List.class.isAssignableFrom(field.getType())) {
                            @SuppressWarnings("unchecked")
                            List<String> values = (List<String>) value;

                            jsonGenerator.writeObjectField(name, values);
                        } else {
                            jsonGenerator.writeObjectField(nameTransformer.transform(name), value);
                        }
                    // }
                // }
            }
        }
    }

    // private ObjectNode processValue(
    //     Map<String, Object> content,
    //     Class<?> type,
    //     Field field,
    //     String[] valueParts,
    //     int index
    // ) {
    //     ObjectNode node = JsonNodeFactory.instance.objectNode();
    //     NestedObject nestedObject = field.getAnnotation(NestedObject.class);
    //     if (nestedObject != null) {
    //         node.put(ID, valueParts[index]);
    //         node.put(nestedObject.label(), valueParts[0]);
    //         processNestedObject(content, type, nestedObject, node, valueParts, index + 1);
    //     }
    //     return node;
    // }

    // private void processNestedObject(
    //     Map<String, Object> content,
    //     Class<?> type,
    //     NestedObject nestedObject,
    //     ObjectNode node,
    //     String[] valueParts,
    //     int depth
    // ) {
    //     for (Reference reference : nestedObject.properties()) {
    //         String ref = reference.value();
    //         Field nestedField = FieldUtils.getField(type, ref, true);
    //         JsonProperty jsonProperty = nestedField.getAnnotation(JsonProperty.class);
    //         String name = jsonProperty != null ? jsonProperty.value() : reference.key();
    //         Object nestedValue = content.get(nestedField.getName());
    //         if (nestedValue != null) {
    //             if (List.class.isAssignableFrom(nestedField.getType())) {

    //                 @SuppressWarnings("unchecked")
    //                 List<String> nestedValues = (List<String>) nestedValue;

    //                 if (nestedValues.size() > 0) {
    //                     boolean multiValued = nestedField.getAnnotation(NestedMultiValuedProperty.class) != null;

    //                     ArrayNode array;

    //                     if (strip(nestedValues.get(0)).split(NESTED_DELIMITER).length > depth) {
    //                         array = nestedValues.parallelStream()
    //                             .filter(nv -> isProperty(valueParts, nv))
    //                             .map(nv -> processValue(content, type, nestedField, strip(nv)
    //                                 .split(NESTED_DELIMITER), depth))
    //                             .collect(new JsonNodeArrayNodeCollector());
    //                     } else {
    //                         array = nestedValues.parallelStream()
    //                             .filter(nv -> isProperty(valueParts, nv))
    //                             .map(nv -> strip(nv).split(NESTED_DELIMITER)[0])
    //                             .collect(new StringArrayNodeCollector());
    //                     }

    //                     if (array.size() > 0) {
    //                         if (multiValued) {
    //                             node.set(name, array);
    //                         } else {
    //                             node.set(name, array.get(0));
    //                         }
    //                     }
    //                 }
    //             } else {
    //                 String[] nestedValueParts = strip(nestedValue.toString()).split(NESTED_DELIMITER);
    //                 if (nestedValueParts.length > depth) {
    //                     node.set(name, processValue(content, type, nestedField, nestedValueParts, depth));
    //                 } else {
    //                     if (nestedValueParts[0] != null) {
    //                         node.put(name, nestedValueParts[0]);
    //                     }
    //                 }
    //             }
    //         }
    //     }
    // }

    // private String strip(String value) {
    //     if (value.startsWith("[") && value.endsWith("]")) {
    //         return value.substring(1, value.length() - 1);
    //     }
    //     return value;
    // }

    // private boolean isProperty(String[] parts, String value) {
    //     for (int i = parts.length - 1; i > 0; i--) {
    //         if (!value.contains(parts[i])) {
    //             return false;
    //         }
    //     }
    //     return true;
    // }

    // private class JsonNodeArrayNodeCollector implements Collector<JsonNode, ArrayNode, ArrayNode> {

    //     @Override
    //     public Supplier<ArrayNode> supplier() {
    //         return mapper::createArrayNode;
    //     }

    //     @Override
    //     public BiConsumer<ArrayNode, JsonNode> accumulator() {
    //         return ArrayNode::add;
    //     }

    //     @Override
    //     public BinaryOperator<ArrayNode> combiner() {
    //         return (x, y) -> {
    //             x.addAll(y);
    //             return x;
    //         };
    //     }

    //     @Override
    //     public Function<ArrayNode, ArrayNode> finisher() {
    //         return accumulator -> accumulator;
    //     }

    //     @Override
    //     public Set<Characteristics> characteristics() {
    //         return EnumSet.of(Characteristics.UNORDERED);
    //     }

    // }

    // private class StringArrayNodeCollector implements Collector<String, ArrayNode, ArrayNode> {

    //     @Override
    //     public Supplier<ArrayNode> supplier() {
    //         return mapper::createArrayNode;
    //     }

    //     @Override
    //     public BiConsumer<ArrayNode, String> accumulator() {
    //         return ArrayNode::add;
    //     }

    //     @Override
    //     public BinaryOperator<ArrayNode> combiner() {
    //         return (x, y) -> {
    //             x.addAll(y);
    //             return x;
    //         };
    //     }

    //     @Override
    //     public Function<ArrayNode, ArrayNode> finisher() {
    //         return accumulator -> accumulator;
    //     }

    //     @Override
    //     public Set<Characteristics> characteristics() {
    //         return EnumSet.of(Characteristics.UNORDERED);
    //     }

    // }

}
