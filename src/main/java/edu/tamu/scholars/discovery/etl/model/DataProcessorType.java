package edu.tamu.scholars.discovery.etl.model;

import edu.tamu.scholars.discovery.etl.DataProcessor;

public interface DataProcessorType {

    String[] getRequiredAttributes();

    DataProcessor getDataProcessor();

}
