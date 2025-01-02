package edu.tamu.scholars.discovery.factory.index;

import java.util.Collection;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;

public interface Index<D> {

    Stream<JsonNode> fieldTypes();

    Stream<JsonNode> fields();

    Stream<JsonNode> copyFields();

    JsonNode schema(JsonNode schema);

    void update(Collection<D> documents);

    void update(D document);

    default void close() {

    }

}
