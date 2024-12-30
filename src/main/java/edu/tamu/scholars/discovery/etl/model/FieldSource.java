package edu.tamu.scholars.discovery.etl.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Embeddable
public class FieldSource extends Source {

    private static final long serialVersionUID = -634950875363070743L;

    @Column(name = "\"unique\"", nullable = false)
    private boolean unique;

    @Column(nullable = false)
    private boolean parse;

    @Column(nullable = false)
    private boolean split;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<CacheableSource> cacheableSources;

    public FieldSource() {
        super();
        this.unique = false;
        this.parse = false;
        this.split = false;
        this.cacheableSources = new HashSet<>();
    }

}
