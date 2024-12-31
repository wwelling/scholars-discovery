package edu.tamu.scholars.discovery.etl.model;

import java.util.HashSet;
import java.util.Set;

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
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@Entity
@Table(
    name = "data",
    indexes = {
        @Index(name = "idx_data_name", columnList = "name")
    }
)
@NamedEntityGraph(
    name = "Data.Graph",
    attributeNodes = {
        @NamedAttributeNode(value = "extractor", subgraph = "processorGraph"),
        @NamedAttributeNode(value = "transformer", subgraph = "processorGraph"),
        @NamedAttributeNode(value = "loader", subgraph = "processorGraph"),
        @NamedAttributeNode(value = "fields", subgraph = "fieldGraph")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "processorGraph",
            attributeNodes = {
                @NamedAttributeNode(value = "attributes")
            }
        ),
        @NamedSubgraph(
            name = "fieldGraph",
            attributeNodes = {
                @NamedAttributeNode(value = "descriptor")
            }
        )
    }
)
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
    private Set<DataField> fields;

    public Data() {
        super();
        this.collectionSource = new CollectionSource();
        this.fields = new HashSet<>();
    }

}
