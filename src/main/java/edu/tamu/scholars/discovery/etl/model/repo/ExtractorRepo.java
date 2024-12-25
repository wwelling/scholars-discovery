package edu.tamu.scholars.discovery.etl.model.repo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.scholars.discovery.etl.model.DataExtractorType;
import edu.tamu.scholars.discovery.etl.model.Extractor;

@RepositoryRestResource
public interface ExtractorRepo extends ConfigurableProcessorRepo<DataExtractorType, Extractor> {

}