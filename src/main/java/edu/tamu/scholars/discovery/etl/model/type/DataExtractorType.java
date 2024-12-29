package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.component.Source;
import edu.tamu.scholars.discovery.component.triplestore.Triplestore;
import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.extract.TriplestoreExtractor;
import edu.tamu.scholars.discovery.etl.model.Data;

public enum DataExtractorType implements DataProcessorType<DataExtractor<?>, Source<?, ?, ?>> {

    TDB_TRIPLESTORE(Triplestore.class, "directory") {
        @Override
        public DataExtractor<?> getDataProcessor(Data data, Source<?, ?, ?> service) {
            return new TriplestoreExtractor(data, (Triplestore) service);
        }
    };

    private final Class<? extends Source<?, ?, ?>> serviceType;

    private final String[] requiredAttributes;

    DataExtractorType(Class<? extends Source<?, ?, ?>> serviceType, String... requiredAttributes) {
        this.serviceType = serviceType;
        this.requiredAttributes = requiredAttributes;
    }

    @Override
    public Class<? extends Source<?, ?, ?>> getServiceType() {
        return serviceType;
    }

    @Override
    public String[] getRequiredAttributes() {
        return requiredAttributes;
    }

}
