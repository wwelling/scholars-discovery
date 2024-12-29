package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.component.Source;
import edu.tamu.scholars.discovery.component.triplestore.Triplestore;
import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.extract.TriplestoreExtractor;
import edu.tamu.scholars.discovery.etl.model.Data;

public enum DataExtractorType implements DataProcessorType<DataExtractor<?>, Source<?, ?, ?>> {

    // TODO: allow overriding required attributes from properties

    TDB_TRIPLESTORE("directory") {
        @Override
        public DataExtractor<?> getDataProcessor(Data data, Source<?, ?, ?> service) {
            return new TriplestoreExtractor(data, (Triplestore) service);
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
