package edu.tamu.scholars.discovery.etl.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Embeddable
public class CacheableSource extends Source {

    public CacheableSource() {
        super();
    }

}
