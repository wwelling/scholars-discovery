package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.model.Extractor;
import edu.tamu.scholars.discovery.etl.model.type.DataExtractorType;

@Component
@RepositoryEventHandler(Extractor.class)
public class ExtractorEventHandler extends ConfigurableProcessorEventHandler<DataExtractor<?>, DataExtractorType, Extractor> {

}
