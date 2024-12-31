package edu.tamu.scholars.discovery.factory.triplestore;

import java.util.Iterator;

import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;

public interface Triplestore {

    Iterator<Triple> collection(String query);

    Model individual(String query);

    default void close() {

    }

}
