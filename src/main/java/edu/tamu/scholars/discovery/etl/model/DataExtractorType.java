package edu.tamu.scholars.discovery.etl.model;

import edu.tamu.scholars.discovery.etl.DataProcessor;

public enum DataExtractorType implements DataProcessorType {

    TDB_SPARQL_HARVESTER("directory") {
        @Override
        public DataProcessor getDataProcessor() {
            // return implemented extractor
            return null;
        }
    },
    HTTP_SPARQL_HARVESTER("url") {
        @Override
        public DataProcessor getDataProcessor() {
            // return implemented extractor
            return null;
        }
    };

    private final String[] requiredAttributes;

    DataExtractorType(String... requiredAttributes) {
        this.requiredAttributes = requiredAttributes;
    }

    @Override
    public String[] getRequiredAttributes() {
        return requiredAttributes;
    }

}
