package edu.tamu.scholars.discovery.etl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.model.repo.listener.ExtractorEntityListener;
import edu.tamu.scholars.discovery.etl.model.type.DataExtractorType;

@Getter
@Setter
@Entity
@EntityListeners(ExtractorEntityListener.class)
@Table(
    name = "extractors",
    indexes = {
        @Index(name = "idx_extractor_name", columnList = "name")
    }
)
public class Extractor extends ConfigurableProcessor<DataExtractor<?>, DataExtractorType> {

    private static final long serialVersionUID = -945672019283746384L;

    public Extractor() {
        super();
    }

}
