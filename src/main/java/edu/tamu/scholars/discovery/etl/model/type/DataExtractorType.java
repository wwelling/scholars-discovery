package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.extract.TdbSparqlExtractor;
import edu.tamu.scholars.discovery.etl.model.Data;

public enum DataExtractorType implements DataProcessorType<DataExtractor<?>> {

    // TODO: allow overriding required attributes from properties

    TDB_SPARQL("directory") {
        @Override
        public DataExtractor<?> getDataProcessor(Data data) {
            return new TdbSparqlExtractor(data);
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
