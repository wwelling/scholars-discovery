package edu.tamu.scholars.discovery.etl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private boolean required = false;

    @Column(nullable = false)
    private boolean readonly = false;

    @Column(nullable = false)
    private boolean stored = true;

    @Column(nullable = false)
    private boolean indexed = true;

    @Column(nullable = false)
    private boolean docValues = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transformer_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Transformer transformer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loader_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Loader loader;

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
