package edu.tamu.scholars.discovery.etl.model;

import java.io.Serializable;

import jakarta.persistence.Column;

public abstract class Source implements Serializable {

    @Column(nullable = false)
    private String template;

    @Column(nullable = false)
    private String predicate;

}
