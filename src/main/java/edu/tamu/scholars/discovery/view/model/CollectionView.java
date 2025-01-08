package edu.tamu.scholars.discovery.view.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
// TODO: validate in handler before create and save
public abstract class CollectionView extends View {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Layout layout;

    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "\"key\"")
    @Column(name = "template", columnDefinition = "TEXT")
    private Map<String, String> templates;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> styles;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> fields;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Facet> facets;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Filter> filters;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Boost> boosts;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Sort> sort;

    @ElementCollection(fetch = FetchType.LAZY)
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
