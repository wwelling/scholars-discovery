package edu.tamu.scholars.discovery.etl.load;

import java.util.Collection;

import edu.tamu.scholars.discovery.etl.DataProcessor;

public interface DataLoader<O> extends DataProcessor {

    public void load(Collection<O> documents);

    public void load(O document);

}