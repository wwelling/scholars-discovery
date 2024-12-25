package edu.tamu.scholars.discovery.view.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort.Direction;

/**
 * Embeddable class `Sort` to describe ordering of {@link View} result sets.
 */
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Sort implements Serializable {

    private static final long serialVersionUID = 192837465746382920L;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Column(nullable = false)
    private boolean isDate = false;

}
