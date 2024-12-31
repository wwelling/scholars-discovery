package edu.tamu.scholars.discovery.factory.triplestore;

import java.util.Iterator;

import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;

public interface Triplestore {

    Iterator<Triple> queryCollection(String query);

    Model queryIndividual(String query);

    default void close() {

    }

}
