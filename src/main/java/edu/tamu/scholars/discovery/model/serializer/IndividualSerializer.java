package edu.tamu.scholars.discovery.model.serializer;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;

import edu.tamu.scholars.discovery.model.Individual;

@JsonComponent
public class IndividualSerializer extends StdSerializer<Individual> {

    public IndividualSerializer() {
        super(Individual.class);
    }

    @Override
    public void serialize(Individual value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        for (Map.Entry<String, Object> entry : value.getContent().entrySet()) {
            gen.writeObjectField(entry.getKey(), entry.getValue());
        }
    }

}
