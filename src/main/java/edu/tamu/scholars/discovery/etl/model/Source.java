package edu.tamu.scholars.discovery.etl.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Source implements Serializable {

    @Column(nullable = false)
    private String template;

    @Column(nullable = false)
    private String predicate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extractor_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Extractor extractor;

}
