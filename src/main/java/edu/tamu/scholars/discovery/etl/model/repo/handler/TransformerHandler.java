package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.etl.model.Transformer;
import edu.tamu.scholars.discovery.etl.model.type.DataTransformerType;
import edu.tamu.scholars.discovery.etl.transform.DataTransformer;

@RepositoryEventHandler(Transformer.class)
public class TransformerHandler extends ConfigurableProcessorHandler<DataTransformer<?, ?>, Mapper, DataTransformerType, Transformer> {

}
