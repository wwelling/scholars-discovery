package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.transform.DataTransformer;
import edu.tamu.scholars.discovery.etl.transform.solr.FlatMapToSolrInputDocumentTransformer;

public enum DataTransformerType implements DataProcessorType<DataTransformer<?, ?>> {

    FLAT_MAP_TO_SOLR_INPUT_DOCUMENT() {
        @Override
        public DataTransformer<?, ?> getDataProcessor(Data data) {
            return new FlatMapToSolrInputDocumentTransformer(data);
        }
    };

    private final String[] requiredAttributes;

    DataTransformerType(String... requiredAttributes) {
        this.requiredAttributes = requiredAttributes;
    }

    @Override
    public String[] getRequiredAttributes() {
        return requiredAttributes;
    }

}
