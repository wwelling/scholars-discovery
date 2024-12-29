package edu.tamu.scholars.discovery.etl.model.repo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.etl.model.Transformer;
import edu.tamu.scholars.discovery.etl.model.type.DataTransformerType;
import edu.tamu.scholars.discovery.etl.transform.DataTransformer;

@RepositoryRestResource
public interface TransformerRepo extends ConfigurableProcessorRepo<DataTransformer<?, ?>, Mapper, DataTransformerType, Transformer> {

}
