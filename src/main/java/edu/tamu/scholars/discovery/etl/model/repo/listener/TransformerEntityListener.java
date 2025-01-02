package edu.tamu.scholars.discovery.etl.model.repo.listener;

import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.etl.model.Transformer;
import edu.tamu.scholars.discovery.etl.model.type.DataTransformerType;
import edu.tamu.scholars.discovery.etl.transform.DataTransformer;

@Component
public class TransformerEntityListener extends ConfigurableProcessorEntityListener<DataTransformer<?, ?>, DataTransformerType, Transformer> {

}
