package edu.tamu.scholars.discovery.component;

import java.util.Iterator;

public interface Source<T, M, Q> extends Service {

    public Iterator<T> queryCollection(Q query);

    public M queryIndividual(Q query);

    public M queryIndividual(String query);

}
