package edu.tamu.scholars.discovery.etl;

public interface DataProcessor {

    public void init();

    public void preProcess();

    public void postProcess();

    public void destroy();

}