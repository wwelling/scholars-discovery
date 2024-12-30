package edu.tamu.scholars.discovery.etl.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Identified;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "nested_references")
public class NestedReference extends Identified {

    private static final long serialVersionUID = 234598273529387456L;

    @ManyToOne(
        cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
        },
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "data_field_descriptor_id", nullable = false)
    @JsonBackReference
    private DataFieldDescriptor descriptor;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String field;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String key;

    @Column(nullable = false)
    private boolean multiValued;

    public NestedReference() {
        super();
        this.multiValued = false;
    }

}
