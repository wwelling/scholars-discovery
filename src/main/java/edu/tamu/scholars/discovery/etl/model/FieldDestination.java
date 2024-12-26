package edu.tamu.scholars.discovery.etl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
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

    @ElementCollection
    private List<String> copyTo;

    @Column(nullable = false)
    private boolean required;

    @Column(nullable = false)
    private boolean readonly;

    @Column(nullable = false)
    private boolean stored;

    @Column(nullable = false)
    private boolean indexed;

    @Column(nullable = false)
    private boolean docValues;

    public FieldDestination() {
        super();
        this.copyTo = new ArrayList<>();
        this.required = false;
        this.readonly = false;
        this.stored = true;
        this.indexed = true;
        this.docValues = false;
    }

}
