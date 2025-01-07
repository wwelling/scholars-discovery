package edu.tamu.scholars.discovery.model.serializer;

import static edu.tamu.scholars.discovery.AppConstants.COLLECTIONS;
import static edu.tamu.scholars.discovery.AppConstants.ID;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import edu.tamu.scholars.discovery.model.Individual;

public class UnwrappingIndividualSerializer extends JsonSerializer<Individual> {

    private static final char DOT = '.';
    private static final char FORWARD_SLASH = '/';

    private static final Set<String> EXCLUDED_FIELDS = Set.of(
        "_root_",
        "_version_",
        "_collections_",
        "_sync_ids_"
    );

    @Override
    public boolean isUnwrappingSerializer() {
        return true;
    }

    @Override
    public void serialize(
        Individual individual,
        JsonGenerator generator,
        SerializerProvider provider
    ) throws IOException {
        Map<?, ?> content = individual.getContent();

        List<String> collections = (List<String>) content.remove(COLLECTIONS);

        serializeContent(individual.getContent(), collections, generator, 1);
    }

    private void serializeContent(
        Map<?, ?> content,
        List<String> collections,
        JsonGenerator generator,
        int depth
    ) throws IOException {
        for (Map.Entry<?, ?> entry : content.entrySet()) {
            serializeContent(
                entry,
                collections,
                generator,
                depth
            );
        }
    }

    private void serializeContent(
        Map.Entry<?, ?> entry,
        List<String> collections,
        JsonGenerator generator,
        int depth
    ) throws IOException {
        String key = (String) entry.getKey();

        if (EXCLUDED_FIELDS.contains(key)) {
            return;
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

                generator.writeStartObject();
                serializeContent(document, collections, generator, depth + 1);
                generator.writeEndObject();

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

    private void serializeCollection(
        Collection<?> collection,
        List<String> collections,
        JsonGenerator generator,
        int depth
    ) throws IOException {
        Object entry = collection.iterator().next();
        boolean isDocument = entry instanceof Map<?, ?>;
        if (depth == 1 || collection.size() > 1 || isDocument) {
            generator.writeStartArray();
            if (isDocument) {
                for (Object document : collection) {
                    generator.writeStartObject();
                    serializeContent((Map<?, ?>) document, collections, generator, depth + 1);
                    generator.writeEndObject();
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
