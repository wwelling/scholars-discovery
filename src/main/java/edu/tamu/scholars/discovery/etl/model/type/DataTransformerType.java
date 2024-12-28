package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.transform.DataTransformer;
import edu.tamu.scholars.discovery.etl.transform.RdfToSolrDocumentTransformer;

public enum DataTransformerType implements DataProcessorType<DataTransformer<?, ?>> {

    // TODO: allow overriding required attributes from properties

    RDF_TO_SOLR_DOCUMENT() {
        @Override
        public DataTransformer<?, ?> getDataProcessor(Data data) {
            return new RdfToSolrDocumentTransformer(data);
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
