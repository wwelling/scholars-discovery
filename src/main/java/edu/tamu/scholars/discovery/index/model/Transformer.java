package edu.tamu.scholars.discovery.index.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transformers")
public class Transformer extends Named {

    private static final long serialVersionUID = -123987654321234987L;

}
