package edu.tamu.scholars.discovery.etl.model;

import edu.tamu.scholars.discovery.etl.DataProcessor;

public enum DataTransformerType implements DataProcessorType {

    RDF_TO_SOLR_DOCUMENT() {
        @Override
        public DataProcessor getDataProcessor() {
            // return implemented transformer
            return null;
        }
    };

    private final String[] requiredAttributes;

    DataTransformerType(String... requiredAttributes) {
        this.requiredAttributes = requiredAttributes;
    }

    @Override
    public String[] getRequiredAttributes() {
        return requiredAttributes;
    }

}
