package edu.tamu.scholars.discovery.etl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class NestedField implements Serializable {

    private static final long serialVersionUID = -242356234956394856L;

    @ElementCollection
    private List<DataFieldDescriptor> fields = new ArrayList<>();

}
