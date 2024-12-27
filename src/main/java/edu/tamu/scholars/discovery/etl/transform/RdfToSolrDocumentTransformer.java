package edu.tamu.scholars.discovery.etl.transform;

import java.util.List;
import java.util.Map;

import edu.tamu.scholars.discovery.etl.model.DataField;

public class RdfToSolrDocumentTransformer implements DataTransformer<Map<String, Object>, Map<String, Object>> {

    private final Map<String, String> properties;

    public RdfToSolrDocumentTransformer(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void init() {

    }

    @Override
    public void preProcess(List<DataField> fields) {

    }

    @Override
    public void postProcess(List<DataField> fields) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public Map<String, Object> transform(Map<String, Object> data) {
        return data;
    }

}
