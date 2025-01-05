package edu.tamu.scholars.discovery.factory.index;

import java.util.Collection;

public interface Index<D> extends Schema, Search {

    void update(Collection<D> documents);

    void update(D document);

    String collection();

    int ping();

    void optimize();

    default void close() {

    }

}
