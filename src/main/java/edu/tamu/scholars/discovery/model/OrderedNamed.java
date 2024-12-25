package edu.tamu.scholars.discovery.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class OrderedNamed extends Named {

    @Min(1)
    @NotNull
    @Column(name = "\"order\"", nullable = false)
    private int order;

}
