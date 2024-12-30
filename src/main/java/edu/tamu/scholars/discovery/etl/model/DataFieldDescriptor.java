package edu.tamu.scholars.discovery.etl.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
    },
    uniqueConstraints = @UniqueConstraint(
        columnNames = {
            "name",
            "default_value",
            "doc_values",
            "indexed",
            "multi_valued",
            "required",
            "stored",
            "type",
            "nested",
            "root",
            "parse",
            "split",
            "unique",
            "predicate",
            "template"
        }
    )
)
@AttributeOverride(name = "name", column = @Column(nullable = false))
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

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        mappedBy = "descriptor"
    )
    @JsonManagedReference
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
