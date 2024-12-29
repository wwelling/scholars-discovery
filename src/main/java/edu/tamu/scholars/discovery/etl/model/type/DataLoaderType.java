package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.component.Destination;
import edu.tamu.scholars.discovery.component.index.Index;
import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.load.IndexLoader;
import edu.tamu.scholars.discovery.etl.model.Data;

public enum DataLoaderType implements DataProcessorType<DataLoader<?>, Destination> {

    // TODO: allow overriding required attributes from properties

    SOLR_INDEXER("host", "collection") {
        @Override
        public DataLoader<?> getDataProcessor(Data data, Destination service) {
            return new IndexLoader(data, (Index) service);
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
