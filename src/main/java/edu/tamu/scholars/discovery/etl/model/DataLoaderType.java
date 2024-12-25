package edu.tamu.scholars.discovery.etl.model;

import edu.tamu.scholars.discovery.etl.DataProcessor;

public enum DataLoaderType implements DataProcessorType {

    SOLR_INDEXER("host", "collection") {
        @Override
        public DataProcessor getDataProcessor() {
            // return implemented loaders
            return null;
        }
    };

    private final String[] requiredAttributes;

    DataLoaderType(String... requiredAttributes) {
        this.requiredAttributes = requiredAttributes;
    }

    @Override
    public String[] getRequiredAttributes() {
        return requiredAttributes;
    }

}
