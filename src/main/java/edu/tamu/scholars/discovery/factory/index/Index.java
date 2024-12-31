package edu.tamu.scholars.discovery.factory.index;

import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;

public interface Index<I, C, R, D> {

    Stream<I> fields();

    Stream<C> copyFields();

    R schema(JsonNode schema);

    R update(D update);

    default void close() {

    }

}
