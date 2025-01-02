package edu.tamu.scholars.discovery.factory.triplestore;

import java.util.Iterator;

public interface Triplestore<T, M> {

    Iterator<T> collection(String query);

    M individual(String query);

    default void close() {

    }

}
