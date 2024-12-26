package edu.tamu.scholars.discovery.etl.model;

import java.util.Map;

import edu.tamu.scholars.discovery.etl.transform.DataTransformer;
import edu.tamu.scholars.discovery.etl.transform.RdfToSolrDocumentTransformer;

public enum DataTransformerType implements DataProcessorType<DataTransformer<?, ?>> {

    RDF_TO_SOLR_DOCUMENT() {
        @Override
        public DataTransformer<?, ?> getDataProcessor(Map<String, String> properties) {
            return new RdfToSolrDocumentTransformer(properties);
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
