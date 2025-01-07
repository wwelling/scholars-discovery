package edu.tamu.scholars.discovery.view.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class FieldView extends OrderedView {

    @Column(nullable = false)
    private String field;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Filter> filters;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Sort> sort;

    protected FieldView() {
        this.filters = new HashSet<>();
        this.sort = new HashSet<>();
    }

}
