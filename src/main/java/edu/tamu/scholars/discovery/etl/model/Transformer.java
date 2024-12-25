package edu.tamu.scholars.discovery.etl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transformers")
public class Transformer extends ConfigurableProcessor<DataTransformerType> {

    private static final long serialVersionUID = -123987654321234987L;

}
