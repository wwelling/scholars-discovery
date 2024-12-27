package edu.tamu.scholars.discovery.etl;

import java.util.Map;

public interface DataProcessor {

    default void init(Map<String, String> propertyOverrides) {

    }

    default void preProcess() {

    }

    default void postProcess() {

    }

    default void destroy() {

    }

}