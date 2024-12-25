package edu.tamu.scholars.discovery.view.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.OpKey;

/**
 * Embeddable class `Filter` to describe filtering of {@link View} result sets.
 */
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Filter implements Serializable {

    private static final long serialVersionUID = -213456789987654321L;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpKey opKey = OpKey.EQUALS;

}
