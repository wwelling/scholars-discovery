package edu.tamu.scholars.discovery.etl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class NestedField implements Serializable {

    private static final long serialVersionUID = -242356234956394856L;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<DataFieldDescriptor> fields;

    public NestedField() {
        this.fields = new ArrayList<>();
    }

}
