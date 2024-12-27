package edu.tamu.scholars.discovery.etl.load;

import java.util.Collection;

import edu.tamu.scholars.discovery.etl.DataProcessor;

public interface DataLoader<I> extends DataProcessor {

    public void load(Collection<I> documents);

    public void load(I document);

}