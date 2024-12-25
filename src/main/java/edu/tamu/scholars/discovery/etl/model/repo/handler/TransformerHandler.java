package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.scholars.discovery.etl.model.DataTransformerType;
import edu.tamu.scholars.discovery.etl.model.Transformer;

@RepositoryEventHandler(Transformer.class)
public class TransformerHandler extends ConfigurableProcessorHandler<DataTransformerType, Transformer> {

}
