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
@Table(name = "extractors")
public class Extractor extends Named {

    private static final long serialVersionUID = -945672019283746384L;

}
