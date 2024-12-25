package edu.tamu.scholars.discovery.etl.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class FieldSource extends Source {

    private static final long serialVersionUID = -634950875363070743L;

    @Column(name = "\"unique\"", nullable = false)
    private boolean unique;

    @Column(nullable = false)
    private boolean parse;

    @Column(nullable = false)
    private boolean split;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extractor_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Extractor extractor;

    public FieldSource() {
        super();
        this.unique = false;
        this.parse = false;
        this.split = false;
    }

}
