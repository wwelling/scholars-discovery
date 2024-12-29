package edu.tamu.scholars.discovery.etl.model;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.component.Service;
import edu.tamu.scholars.discovery.etl.DataProcessor;
import edu.tamu.scholars.discovery.etl.model.type.DataProcessorType;
import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@MappedSuperclass
public abstract class ConfigurableProcessor<P extends DataProcessor, S extends Service, T extends DataProcessorType<P, S>> extends Named {

    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "key")
    @Column(name = "attribute", columnDefinition = "TEXT")
    private Map<String, String> attributes;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private T type;

    protected ConfigurableProcessor() {
        super();
        this.attributes = new HashMap<>();
    }

}
