package edu.tamu.scholars.discovery.etl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "loaders",
    indexes = {
        @Index(name = "idx_loader_name", columnList = "name")
})
public class Loader extends ConfigurableProcessor<DataLoaderType> {

    private static final long serialVersionUID = 846236512763254198L;

}
