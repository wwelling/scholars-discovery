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

/**
 * A persistent representation of how a UI should render a collection view and its result set.
 */
@MappedSuperclass
// @ValidCollectionFacets(message = "{CollectionView.validCollectionFacets}")
// @ValidCollectionFilters(message = "{CollectionView.validCollectionFilters}")
// @ValidCollectionExport(message = "{CollectionView.validCollectionExport}")
public abstract class CollectionView extends View {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Layout layout;

    @ElementCollection
    @MapKeyColumn(name = "key")
    @Column(name = "template", columnDefinition = "TEXT")
    private Map<String, String> templates = new HashMap<>();

    @ElementCollection
    private List<String> styles = new ArrayList<>();

    @ElementCollection
    private List<String> fields = new ArrayList<>();

    @ElementCollection
    private List<Facet> facets = new ArrayList<>();

    @ElementCollection
    private List<Filter> filters = new ArrayList<>();

    @ElementCollection
    private List<Boost> boosts = new ArrayList<>();

    @ElementCollection
    private List<Sort> sort = new ArrayList<>();

    @ElementCollection
    private List<ExportField> export = new ArrayList<>();

}
