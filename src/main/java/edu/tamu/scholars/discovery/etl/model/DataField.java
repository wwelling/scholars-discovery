package edu.tamu.scholars.discovery.etl.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@Entity
@Table(
    name = "data_fields",
    indexes = {
        @Index(name = "idx_data_field_name", columnList = "name")
    }
)
@SuppressWarnings("java:S2160") // the inherited equals is of id
public class DataField extends Named {

    private static final long serialVersionUID = -284562394634567551L;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private DataFieldDescriptor descriptor;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = "data_field_nested_fields",
        joinColumns = @JoinColumn(name = "data_field_id"),
        inverseJoinColumns = @JoinColumn(name = "data_field_descriptor_id")
    )
    private List<DataFieldDescriptor> nestedFields;

    public DataField() {
        super();
        this.descriptor = new DataFieldDescriptor();
        this.nestedFields = new ArrayList<>();
    }

}
