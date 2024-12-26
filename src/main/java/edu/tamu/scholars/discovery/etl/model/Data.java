package edu.tamu.scholars.discovery.etl.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@Entity
@NamedEntityGraph(
    name = "data-with-fields",
    attributeNodes = {
        @NamedAttributeNode(value = "fields")
    }
)
@NamedEntityGraph(
    name = "data-with-fields-and-processors",
    attributeNodes = {
        @NamedAttributeNode(value = "extractor"),
        @NamedAttributeNode(value = "transformer"),
        @NamedAttributeNode(value = "loader"),
        @NamedAttributeNode(value = "fields"),
    }
)
@Table(
    name = "data",
    indexes = {
        @Index(name = "idx_data_name", columnList = "name")
    }
)
@SuppressWarnings("java:S2160") // the inherited equals is of id
public class Data extends Named {

    @Embedded
    private CollectionSource collectionSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extractor_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Extractor extractor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transformer_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Transformer transformer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loader_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Loader loader;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        mappedBy = "data"
    )
    @JsonManagedReference
    private List<DataField> fields;

    public Data() {
        super();
        this.collectionSource = new CollectionSource();
        this.fields = new ArrayList<>();
    }

}
