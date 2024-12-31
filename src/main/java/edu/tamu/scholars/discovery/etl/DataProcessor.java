package edu.tamu.scholars.discovery.etl;

public interface DataProcessor {

    default void init() {

    }

    default void preprocess() {

    }

    default void postprocess() {

    }

    default void destroy() {

    }

}