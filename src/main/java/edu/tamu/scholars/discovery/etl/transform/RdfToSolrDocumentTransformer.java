package edu.tamu.scholars.discovery.etl.transform;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import edu.tamu.scholars.discovery.etl.model.Data;

@Slf4j
public class RdfToSolrDocumentTransformer implements DataTransformer<Map<String, Object>, Map<String, Object>> {

    private final Data data;

    private final ObjectMapper objectMapper;

    public RdfToSolrDocumentTransformer(Data data) {
        this.data = data;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Map<String, Object> transform(Map<String, Object> data) {

        // try {
        //     final String json = this.objectMapper.writerWithDefaultPrettyPrinter()
        //         .writeValueAsString(data);

        //     System.out.println("\n" + this.data.getName() + "\n" + json + "\n");
        // } catch (JsonProcessingException e) {
        //     e.printStackTrace();
        // }

        return data;
    }

}
