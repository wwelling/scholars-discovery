package edu.tamu.scholars.discovery.etl.model.repo.listener;

import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.model.Extractor;
import edu.tamu.scholars.discovery.etl.model.type.DataExtractorType;

@Component
public class ExtractorEntityListener extends ConfigurableProcessorEntityListener<DataExtractor<?>, DataExtractorType, Extractor> {

}
