package edu.tamu.scholars.discovery.etl.model;

import edu.tamu.scholars.discovery.etl.transform.DataTransformer;
import edu.tamu.scholars.discovery.etl.transform.RdfToSolrDocumentTransformer;

public enum DataTransformerType implements DataProcessorType<DataTransformer<?, ?>> {

    RDF_TO_SOLR_DOCUMENT() {
        @Override
        public DataTransformer<?, ?> getDataProcessor() {
            return new RdfToSolrDocumentTransformer();
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
