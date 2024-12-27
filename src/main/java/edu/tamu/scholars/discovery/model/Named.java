package edu.tamu.scholars.discovery.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@MappedSuperclass
public abstract class Named extends Versioned {

    @Size(min = 2, max = 64, message = "${Named.nameSize}")
    @Column(nullable = false, unique = true)
    @ToString.Include
    protected String name;

    @NotNull(message = "${Named.nameRequired}")
    public String getName() {
        return name;
    }

}
