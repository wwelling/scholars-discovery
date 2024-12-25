package edu.tamu.scholars.discovery.etl.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "data")
public class Data extends Named {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "data_extractors",
        joinColumns = @JoinColumn(name = "data_id"),
        inverseJoinColumns = @JoinColumn(name = "extractor_id")
    )
    private List<Extractor> extractors = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "data_transformers",
        joinColumns = @JoinColumn(name = "data_id"),
        inverseJoinColumns = @JoinColumn(name = "transformer_id")
    )
    private List<Transformer> transformers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "data_loaders",
        joinColumns = @JoinColumn(name = "data_id"),
        inverseJoinColumns = @JoinColumn(name = "loader_id")
    )
    private List<Loader> loaders = new ArrayList<>();

}
