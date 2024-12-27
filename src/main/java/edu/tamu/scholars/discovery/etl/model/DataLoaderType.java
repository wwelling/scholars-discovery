package edu.tamu.scholars.discovery.etl.model;

import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.load.SolrIndexLoader;

public enum DataLoaderType implements DataProcessorType<DataLoader<?>> {

    SOLR_INDEXER("host", "collection") {
        @Override
        public DataLoader<?> getDataProcessor(Data data) {
            return new SolrIndexLoader(data);
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
