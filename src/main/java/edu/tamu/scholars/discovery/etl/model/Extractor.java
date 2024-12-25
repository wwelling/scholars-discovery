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
@Table(name = "extractors")
public class Extractor extends AbstractConfigurable {

    private static final long serialVersionUID = -945672019283746384L;

}
