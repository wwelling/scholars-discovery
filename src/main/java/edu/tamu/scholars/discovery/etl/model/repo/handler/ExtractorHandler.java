package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.scholars.discovery.component.Source;
import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.model.Extractor;
import edu.tamu.scholars.discovery.etl.model.type.DataExtractorType;

@RepositoryEventHandler(Extractor.class)
public class ExtractorHandler extends ConfigurableProcessorHandler<DataExtractor<?>, Source<?, ?, ?>, DataExtractorType, Extractor> {

}
