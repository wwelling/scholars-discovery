package edu.tamu.scholars.discovery.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@MappedSuperclass
public abstract class Versioned extends Identified {

    @Version
    @Column(name = "version")
    @ToString.Include
    private Long version;

}
