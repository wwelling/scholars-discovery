package edu.tamu.scholars.discovery.etl.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@Entity
@Table(
    name = "data_field_descriptors",
    indexes = {
        @Index(name = "idx_data_field_descriptor_name", columnList = "name")
    }
)
public class DataFieldDescriptor extends Named {

    private static final long serialVersionUID = 875973459875978634L;

    @Column(nullable = false)
    public boolean nested;

    @Column(nullable = false)
    public boolean root;

    @Embedded
    private FieldDestination destination;

    @Embedded
    private FieldSource source;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<NestedReference> nestedReferences;

    public DataFieldDescriptor() {
        super();
        this.nested = false;
        this.root = false;
        this.destination = new FieldDestination();
        this.source = new FieldSource();
        this.nestedReferences = new HashSet<>();
    }

}
