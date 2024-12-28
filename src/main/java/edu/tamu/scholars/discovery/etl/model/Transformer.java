package edu.tamu.scholars.discovery.etl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.etl.model.type.DataTransformerType;

@Getter
@Setter
@Entity
@Table(
    name = "transformers",
    indexes = {
        @Index(name = "idx_transformer_name", columnList = "name")
    }
)
public class Transformer extends ConfigurableProcessor<DataTransformerType> {

    private static final long serialVersionUID = -123987654321234987L;

    public Transformer() {
        super();
    }

}
