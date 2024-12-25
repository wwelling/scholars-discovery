package edu.tamu.scholars.discovery.view.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Embeddable domain model for {@link CollectionView} result set term relevancy boosting.
 */
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Boost implements Serializable {

    private static final long serialVersionUID = -123456789012345678L;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    private float value;

}
