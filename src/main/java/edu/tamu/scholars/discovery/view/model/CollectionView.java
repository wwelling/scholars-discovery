package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MappedSuperclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A persistent representation of how a UI should render a collection view and its result set.
 */
@MappedSuperclass
// @ValidCollectionFacets(message = "{CollectionView.validCollectionFacets}")
// @ValidCollectionFilters(message = "{CollectionView.validCollectionFilters}")
// @ValidCollectionExport(message = "{CollectionView.validCollectionExport}")
public abstract class CollectionView extends View {

    private static final long serialVersionUID = 6875458024293994230L;

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

    public CollectionView() {
        super();
        templates = new HashMap<>();
        styles = new ArrayList<>();
        fields = new ArrayList<>();
        facets = new ArrayList<>();
        filters = new ArrayList<>();
        boosts = new ArrayList<>();
        sort = new ArrayList<>();
        export = new ArrayList<>();
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Map<String, String> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }

    public List<String> getStyles() {
        return styles;
    }

    public void setStyles(List<String> styles) {
        this.styles = styles;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<Boost> getBoosts() {
        return boosts;
    }

    public void setBoosts(List<Boost> boosts) {
        this.boosts = boosts;
    }

    public List<Sort> getSort() {
        return sort;
    }

    public void setSort(List<Sort> sort) {
        this.sort = sort;
    }

    public List<ExportField> getExport() {
        return export;
    }

    public void setExport(List<ExportField> export) {
        this.export = export;
    }

}
