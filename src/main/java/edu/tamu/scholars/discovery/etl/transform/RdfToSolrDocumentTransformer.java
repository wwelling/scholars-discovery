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
    public Map<String, Object> transform(Map<String, Object> fromRdf) {

        try {
            String json = this.objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(fromRdf);

            System.out.println("\n" + data.getName() + " " + json + "\n");
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // TODO: group nested objects
        return fromRdf;
    }

}
