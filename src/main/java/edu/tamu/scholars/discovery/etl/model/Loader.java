package edu.tamu.scholars.discovery.etl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.model.type.DataLoaderType;

@Getter
@Setter
@Entity
@Table(
    name = "loaders",
    indexes = {
        @Index(name = "idx_loader_name", columnList = "name")
    }
)
public class Loader extends ConfigurableProcessor<DataLoader<?>, DataLoaderType> {

    private static final long serialVersionUID = 846236512763254198L;

    public Loader() {
        super();
    }

}
