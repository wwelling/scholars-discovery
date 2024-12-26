package edu.tamu.scholars.discovery.etl.model;

import java.util.Map;

import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.extract.HttpSparqlExtractor;
import edu.tamu.scholars.discovery.etl.extract.TdbSparqlExtractor;

public enum DataExtractorType implements DataProcessorType<DataExtractor<?>> {

    TDB_SPARQL("directory") {
        @Override
        public DataExtractor<?> getDataProcessor(Map<String, String> properties) {
            return new TdbSparqlExtractor(properties);
        }
    },
    HTTP_SPARQL("url") {
        @Override
        public DataExtractor<?> getDataProcessor(Map<String, String> properties) {
            return new HttpSparqlExtractor(properties);
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
