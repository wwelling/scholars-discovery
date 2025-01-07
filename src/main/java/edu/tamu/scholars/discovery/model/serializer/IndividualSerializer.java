package edu.tamu.scholars.discovery.model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import org.springframework.boot.jackson.JsonComponent;

import edu.tamu.scholars.discovery.model.Individual;

@JsonComponent
public class IndividualSerializer extends StdSerializer<Individual> {

    private final transient JsonSerializer<Individual> delegate = new UnwrappingIndividualSerializer();

    public IndividualSerializer() {
        super(Individual.class);
    }

    @Override
    public void serialize(
        Individual document,
        JsonGenerator generator,
        SerializerProvider provider
    ) throws IOException {
        generator.writeStartObject();
        delegate.serialize(document, generator, provider);
        generator.writeEndObject();
    }

    @Override
    public JsonSerializer<Individual> unwrappingSerializer(final NameTransformer nameTransformer) {
        return new UnwrappingIndividualSerializer();
    }

}