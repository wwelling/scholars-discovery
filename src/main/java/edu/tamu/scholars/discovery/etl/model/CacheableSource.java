package edu.tamu.scholars.discovery.etl.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class CacheableSource extends Source {

    public CacheableSource() {
        super();
    }

}
