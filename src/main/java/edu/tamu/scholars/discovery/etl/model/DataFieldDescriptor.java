package edu.tamu.scholars.discovery.etl.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.OrderedNamed;

@Getter
@Setter
@Entity
@Table(
    name = "data_field_descriptors",
    indexes = {
        @Index(name = "idx_data_field_descriptor_name", columnList = "name"),
        @Index(name = "idx_data_field_descriptor_order", columnList = "\"order\"")
})
@SuppressWarnings("java:S2160") // the inherited equals is of id
public class DataFieldDescriptor extends OrderedNamed {

    private static final long serialVersionUID = 875973459875978634L;

    @Embedded
    private FieldDestination destination;

    @Embedded
    private FieldSource source;

    public DataFieldDescriptor() {
        super();
        this.destination = new FieldDestination();
        this.source = new FieldSource();
    }

}
