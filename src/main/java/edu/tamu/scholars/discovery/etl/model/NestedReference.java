package edu.tamu.scholars.discovery.etl.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class NestedReference implements Serializable {

    private static final long serialVersionUID = 234598273529387456L;

    @Column(nullable = true)
    private String key;

    @Column(nullable = true)
    private Boolean multiple;

    public NestedReference() {
        super();
    }

}
