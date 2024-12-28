package edu.tamu.scholars.discovery.triplestore;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Model;
import reactor.core.publisher.Flux;

public interface Triplestore {

    Flux<Triple> execConstructTriples(Query query);

    Model execConstruct(Query query);

    Model execConstruct(String query);

    default void init() {

    }

    default void destroy() {

    }

}
