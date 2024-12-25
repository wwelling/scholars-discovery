package edu.tamu.scholars.discovery.etl.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.OrderedNamed;

@Getter
@Setter
@Entity
@Table(
    name = "data_fields",
    indexes = {
        @Index(name = "idx_data_field_order", columnList = "\"order\""),
        @Index(name = "idx_data_field_descriptor_id", columnList = "descriptor_id")
})
@AttributeOverride(name = "name", column = @Column(nullable = false))
@SuppressWarnings("java:S2160") // the inherited equals is of id
public class DataField extends OrderedNamed {

    private static final long serialVersionUID = -284562394634567551L;

    @ManyToOne(fetch = FetchType.LAZY)
    private DataFieldDescriptor descriptor;

    @OrderBy("order")
    @ManyToMany(fetch = FetchType.LAZY)
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
