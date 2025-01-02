package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.etl.model.Transformer;
import edu.tamu.scholars.discovery.etl.model.type.DataTransformerType;
import edu.tamu.scholars.discovery.etl.transform.DataTransformer;

@Component("transformerEventHandler")
@RepositoryEventHandler(Transformer.class)
public class TransformerEventHandler extends ConfigurableProcessorEventHandler<DataTransformer<?, ?>, DataTransformerType, Transformer> {

}
