package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.extract.TdbTriplestoreExtractor;
import edu.tamu.scholars.discovery.etl.model.Data;

public enum DataExtractorType implements DataProcessorType<DataExtractor<?>> {

    TDB_TRIPLESTORE("directory") {
        @Override
        public DataExtractor<?> getDataProcessor(Data data) {
            return new TdbTriplestoreExtractor(data);
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
