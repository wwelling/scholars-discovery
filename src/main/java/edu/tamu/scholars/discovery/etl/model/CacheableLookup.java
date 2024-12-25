package edu.tamu.scholars.discovery.etl.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class CacheableLookup implements Serializable {

    private static final long serialVersionUID = 653489578346565983L;

    @Column(nullable = false)
    private String template;

    @Column(nullable = false)
    private String predicate;

}
