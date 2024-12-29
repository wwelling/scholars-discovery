package edu.tamu.scholars.discovery.etl.model.repo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.scholars.discovery.component.Source;
import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.model.Extractor;
import edu.tamu.scholars.discovery.etl.model.type.DataExtractorType;

@RepositoryRestResource
public interface ExtractorRepo extends ConfigurableProcessorRepo<DataExtractor<?>, Source<?, ?, ?>, DataExtractorType, Extractor> {

}
