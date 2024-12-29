package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.component.Destination;
import edu.tamu.scholars.discovery.component.index.Index;
import edu.tamu.scholars.discovery.component.index.SolrIndex;
import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.load.IndexLoader;
import edu.tamu.scholars.discovery.etl.model.Data;

public enum DataLoaderType implements DataProcessorType<DataLoader<?>, Destination> {

    SOLR_INDEXER(SolrIndex.class, "host", "collection") {
        @Override
        public DataLoader<?> getDataProcessor(Data data, Destination service) {
            return new IndexLoader(data, (Index) service);
        }
    };

    private final Class<? extends Destination> serviceType;

    private final String[] requiredAttributes;

    DataLoaderType(Class<? extends Destination> serviceType, String... requiredAttributes) {
        this.serviceType = serviceType;
        this.requiredAttributes = requiredAttributes;
    }

    @Override
    public Class<? extends Destination> getServiceType() {
        return serviceType;
    }

    @Override
    public String[] getRequiredAttributes() {
        return requiredAttributes;
    }

}
