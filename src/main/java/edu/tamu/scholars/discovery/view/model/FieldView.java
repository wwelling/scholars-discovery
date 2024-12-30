package edu.tamu.scholars.discovery.view.model;

import java.util.ArrayList;
import java.util.List;

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
    public String field;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Filter> filters;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Sort> sort;

    protected FieldView() {
        this.filters = new ArrayList<>();
        this.sort = new ArrayList<>();
    }

}
