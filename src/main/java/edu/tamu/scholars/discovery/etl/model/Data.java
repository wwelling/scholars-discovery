package edu.tamu.scholars.discovery.etl.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "data",
    indexes = {
        @Index(name = "idx_data_name", columnList = "name")
})
public class Data extends Named {

    @Embedded
    private CollectionSource collectionSource = new CollectionSource();

    @OrderBy("order")
    @JoinColumn(name = "data_field_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DataField> fields = new ArrayList<>();

}
