package edu.tamu.scholars.discovery.etl.model;

import java.util.Map;

import edu.tamu.scholars.discovery.etl.DataProcessor;

public interface DataProcessorType<P extends DataProcessor> {

    String[] getRequiredAttributes();

    P getDataProcessor(Map<String, String> properties);

}
