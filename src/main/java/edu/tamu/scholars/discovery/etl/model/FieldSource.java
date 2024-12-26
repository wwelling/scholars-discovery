package edu.tamu.scholars.discovery.etl.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class FieldSource extends Source {

    private static final long serialVersionUID = -634950875363070743L;

    @Column(name = "\"unique\"", nullable = false)
    private boolean unique;

    @Column(nullable = false)
    private boolean parse;

    @Column(nullable = false)
    private boolean split;

    public FieldSource() {
        super();
        this.unique = false;
        this.parse = false;
        this.split = false;
    }

}
