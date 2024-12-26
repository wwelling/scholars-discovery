package edu.tamu.scholars.discovery.etl.model;

import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.extract.HttpSparqlExtractor;
import edu.tamu.scholars.discovery.etl.extract.TdbSparqlExtractor;

public enum DataExtractorType implements DataProcessorType<DataExtractor<?>> {

    TDB_SPARQL("directory") {
        @Override
        public DataExtractor<?> getDataProcessor() {
            return new TdbSparqlExtractor();
        }
    },
    HTTP_SPARQL("url") {
        @Override
        public DataExtractor<?> getDataProcessor() {
            return new HttpSparqlExtractor();
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
