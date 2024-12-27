package edu.tamu.scholars.discovery.etl.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class FieldDestination implements Serializable {

    private static final long serialVersionUID = 893426523498598375L;

    @Column(nullable = false)
    private String type;

    @Column(nullable = true)
    private String defaultValue;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> copyTo;

    @Column(nullable = false)
    private boolean required;

    @Column(nullable = false)
    private boolean stored;

    @Column(nullable = false)
    private boolean indexed;

    @Column(nullable = false)
    private boolean multiValued;

    @Column(nullable = false)
    private boolean docValues;

    public FieldDestination() {
        super();
        this.copyTo = new HashSet<>();
        this.required = false;
        this.stored = true;
        this.indexed = true;
        this.multiValued = false;
        this.docValues = false;
    }

}
