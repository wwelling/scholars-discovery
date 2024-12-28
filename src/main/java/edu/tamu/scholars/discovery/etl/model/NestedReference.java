package edu.tamu.scholars.discovery.etl.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class NestedReference implements Serializable {

    private static final long serialVersionUID = 234598273529387456L;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    private String key;

    public NestedReference() {
        super();
    }

}
