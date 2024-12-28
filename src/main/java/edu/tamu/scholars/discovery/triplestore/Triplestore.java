package edu.tamu.scholars.discovery.triplestore;

import org.apache.jena.query.QueryExecution;

public interface Triplestore {

    QueryExecution createQueryExecution(String query);

    default void init() {

    }

    default void destroy() {

    }

}
