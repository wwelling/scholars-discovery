package edu.tamu.scholars.discovery.etl.model.repo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.scholars.discovery.etl.model.DataTransformerType;
import edu.tamu.scholars.discovery.etl.model.Transformer;

@RepositoryRestResource
public interface TransformerRepo extends ConfigurableProcessorRepo<DataTransformerType, Transformer> {

}
