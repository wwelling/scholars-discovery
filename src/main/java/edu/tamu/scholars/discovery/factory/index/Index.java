package edu.tamu.scholars.discovery.factory.index;

import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;

public interface Index<I, C, R> {

    Stream<I> getFields();

    Stream<C> getCopyFields();

    R updateSchema(JsonNode schemaRequestNode);

    default void close() {

    }

}
