package edu.tamu.scholars.discovery.model.serializer;

import static edu.tamu.scholars.discovery.AppConstants.COLLECTIONS;
import static edu.tamu.scholars.discovery.AppConstants.ID;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;

import edu.tamu.scholars.discovery.model.Individual;

@JsonComponent
public class IndividualSerializer extends StdSerializer<Individual> {

    private static final char DOT = '.';
    private static final char FORWARD_SLASH = '/';

    private static final Set<String> EXCLUDED_FIELDS = Set.of("_root_", "_version_", "_collections_", "_sync_ids_");

    public IndividualSerializer() {
        super(Individual.class);
    }

    @Override
    public void serialize(Individual individual, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.setHighestNonEscapedChar(127);
        generator.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
        generator.disable(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM);

        Map<?, ?> content = individual.getContent();

        List<String> collections = (List<String>) content.remove(COLLECTIONS);

        serializeContent(individual.getContent(), collections, generator, 0);
    }

    private void serializeContent(Map<?, ?> content, List<String> collections, JsonGenerator generator, int depth) throws IOException {
        depth++;
        for (Map.Entry<?, ?> entry : content.entrySet()) {
            String key = (String) entry.getKey();

            if (EXCLUDED_FIELDS.contains(key)) {
                continue;
            }

            Object value = entry.getValue();

            if (key.equals(ID)) {
                value = getId((String) value);
            }

            String property = getProperty(key);

            if (value instanceof Map<?, ?> document) {
                if (!document.isEmpty()) {
                    generator.writeFieldName(property);

                    boolean isCollection = collections.contains(property);

                    if (isCollection) {
                        generator.writeStartArray();
                    }

                    serializeNestedDocument(document, collections, generator, depth);

                    if (isCollection) {
                        generator.writeEndArray();
                    }
                }
            } else if (value instanceof Collection<?> collection) {
                if (!collection.isEmpty()) {
                    generator.writeFieldName(property);
                    serializeCollection(collection, collections, generator, depth);
                }
            } else {
                generator.writeObjectField(property, value);
            }
        }
    }

    private void serializeNestedDocument(Map<?, ?> document, List<String> collections, JsonGenerator generator, int depth) throws IOException {
        generator.writeStartObject();
        serializeContent(document, collections, generator, depth);
        generator.writeEndObject();
    }

    private void serializeCollection(Collection<?> collection, List<String> collections, JsonGenerator generator, int depth) throws IOException {
        Object entry = collection.iterator().next();
        boolean isDocument = entry instanceof Map<?, ?>;
        if (depth == 1 || collection.size() > 1 || isDocument) {
            generator.writeStartArray();
            if (isDocument) {
                for (Object document : collection) {
                    serializeNestedDocument((Map<?, ?>) document, collections, generator, depth);
                }
            } else {
                for (Object value : collection) {
                    generator.writeObject(value);
                }
            }
            generator.writeEndArray();
        } else {
            generator.writeObject(entry);
        }
    }

    private String getId(String value) {
        return getAfter(value, FORWARD_SLASH);
    }

    private String getProperty(String key) {
        return getAfter(key, DOT);
    }

    private String getAfter(String value, char delimiter) {
        int index = value.lastIndexOf(delimiter);

        return index < 0 ? value : value.substring(index + 1);
    }

}
