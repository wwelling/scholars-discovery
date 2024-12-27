package edu.tamu.scholars.discovery.etl;

public interface DataProcessor {

    default void init() {

    }

    default void preProcess() {

    }

    default void postProcess() {

    }

    default void destroy() {

    }

}