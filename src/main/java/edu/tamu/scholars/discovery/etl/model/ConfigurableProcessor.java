package edu.tamu.scholars.discovery.etl.model;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@MappedSuperclass
public abstract class ConfigurableProcessor<T extends DataProcessorType> extends Named {

    @ElementCollection
    @MapKeyColumn(name = "key")
    @Column(name = "attribute", columnDefinition = "TEXT")
    private Map<String, String> attributes = new HashMap<>();

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private T type;

    @Min(1)
    @Column(name = "\"order\"", nullable = false)
    private int order;

}
