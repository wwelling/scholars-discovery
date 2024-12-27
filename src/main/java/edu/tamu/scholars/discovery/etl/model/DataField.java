package edu.tamu.scholars.discovery.etl.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Versioned;

@Getter
@Setter
@Entity
@Table(name = "data_fields")
@SuppressWarnings("java:S2160") // the inherited equals is of id
public class DataField extends Versioned {

    private static final long serialVersionUID = -284562394634567551L;

    @ManyToOne(
        cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
        },
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "data_id", nullable = false)
    @JsonBackReference
    private Data data;

    @ManyToOne(
        cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
        },
        fetch = FetchType.EAGER,
        optional = false
    )
    private DataFieldDescriptor descriptor;

    @ManyToMany(
        cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
        },
        fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "data_field_nested_fields",
        joinColumns = @JoinColumn(name = "data_field_id"),
        inverseJoinColumns = @JoinColumn(name = "data_field_descriptor_id")
    )
    private List<DataFieldDescriptor> nestedDescriptors;

    public DataField() {
        super();
        this.descriptor = new DataFieldDescriptor();
        this.nestedDescriptors = new ArrayList<>();
    }

}
