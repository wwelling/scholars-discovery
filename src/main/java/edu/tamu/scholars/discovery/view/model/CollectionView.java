package edu.tamu.scholars.discovery.view.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * A persistent representation of how a UI should render a collection view and its result set.
 */
@Getter
@Setter
@MappedSuperclass
// @ValidCollectionFacets(message = "{CollectionView.validCollectionFacets}")
// @ValidCollectionFilters(message = "{CollectionView.validCollectionFilters}")
// @ValidCollectionExport(message = "{CollectionView.validCollectionExport}")
// TODO: validate in handler before create and save
@SuppressWarnings("java:S2160") // the inherited equals is of id
public abstract class CollectionView extends View {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Layout layout;

    @ElementCollection
    @MapKeyColumn(name = "key")
    @Column(name = "template", columnDefinition = "TEXT")
    private Map<String, String> templates;

    @ElementCollection
    private List<String> styles;

    @ElementCollection
    private List<String> fields;

    @ElementCollection
    private List<Facet> facets;

    @ElementCollection
    private List<Filter> filters;

    @ElementCollection
    private List<Boost> boosts;

    @ElementCollection
    private List<Sort> sort;

    @ElementCollection
    private List<ExportField> export;

    protected CollectionView() {
        this.templates = new HashMap<>();
        this.styles = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.facets = new ArrayList<>();
        this.filters = new ArrayList<>();
        this.boosts = new ArrayList<>();
        this.sort = new ArrayList<>();
        this.export = new ArrayList<>();
    }

}
