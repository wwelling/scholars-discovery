package edu.tamu.scholars.discovery.etl.transform;

import java.util.Map;

import edu.tamu.scholars.discovery.etl.model.Data;

public class RdfToSolrDocumentTransformer implements DataTransformer<Map<String, Object>, Map<String, Object>> {

    public RdfToSolrDocumentTransformer(Data data) {

    }

    @Override
    public Map<String, Object> transform(Map<String, Object> data) {
        return data;
    }

}
