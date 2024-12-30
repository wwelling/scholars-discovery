package edu.tamu.scholars.discovery.etl.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Embeddable
public class CollectionSource extends Source {

    private static final long serialVersionUID = -123456789012345678L;

    public CollectionSource() {
        super();
    }

}
