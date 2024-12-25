package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a view for display subsections.
 * Extends the {@link FieldView} class.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "display_subsections")
@AttributeOverride(name = "name", column = @Column(nullable = false))
public class DisplaySubsectionView extends FieldView {

    private static final long serialVersionUID = 140302030405060708L;

    @Column(nullable = false)
    private int pageSize = 5;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String template;

}

