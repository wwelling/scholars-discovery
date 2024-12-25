package edu.tamu.scholars.discovery.etl.model;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MappedSuperclass;

import edu.tamu.scholars.discovery.model.Named;

@MappedSuperclass
public abstract class AbstractConfigurable extends Named {

    @ElementCollection
    @MapKeyColumn(name = "key")
    @Column(name = "attributes", columnDefinition = "TEXT")
    private Map<String, String> attributes = new HashMap<>();

}
