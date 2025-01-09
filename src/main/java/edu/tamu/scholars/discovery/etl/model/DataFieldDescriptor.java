package edu.tamu.scholars.discovery.etl.model;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
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
            "nested",
            "multiple",
            "type",
            "default_value",
            "required",
            "stored",
            "indexed",
            "multi_valued",
            "doc_values",
            "template",
            "predicate",
            "unique",
            "parse",
            "split"
        }
    )
)
@AttributeOverride(name = "name", column = @Column(nullable = false))
public class DataFieldDescriptor extends Named {

    private static final long serialVersionUID = 875973459875978634L;

    @Column(nullable = false)
    private boolean nested;

    @Column(nullable = true)
    private String nestPath;

    @Column(nullable = false)
    private boolean multiple;

    @Embedded
    private FieldDestination destination;

    @Embedded
    private FieldSource source;

    @OneToMany(
        cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
        },
        fetch = FetchType.EAGER,
        orphanRemoval = true
    )
    @JoinColumn(name = "parent_descriptor_id")
    private Set<DataFieldDescriptor> nestedDescriptors;

    public DataFieldDescriptor() {
        super();
        this.nested = false;
        this.multiple = false;
        this.destination = new FieldDestination();
        this.source = new FieldSource();
        this.nestedDescriptors = new HashSet<>();
    }

    public String getKey() {
        return isNotEmpty(this.nestPath)
            ? this.nestPath
            : this.name;
    }

}
