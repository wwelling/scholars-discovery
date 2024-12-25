package edu.tamu.scholars.discovery.view.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract mapped superclass `FieldView` to order itself and
 * filter and sort its result sets for a given field.
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class FieldView extends View {

    @Column(nullable = false)
    public String field;

    @Min(1)
    @Column(name = "\"order\"", nullable = false)
    private int order;

    @ElementCollection
    private List<Filter> filters = new ArrayList<>();

    @ElementCollection
    private List<Sort> sort = new ArrayList<>();

}
