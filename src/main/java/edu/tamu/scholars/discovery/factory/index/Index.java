package edu.tamu.scholars.discovery.factory.index;

import java.util.Collection;

public interface Index<D> extends Schema {

    void update(Collection<D> documents);

    void update(D document);

    void optimize();

    default void close() {

    }

}
