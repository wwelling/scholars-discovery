package edu.tamu.scholars.discovery.etl.model.repo.listener;

import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.model.Loader;
import edu.tamu.scholars.discovery.etl.model.type.DataLoaderType;

@Component
public class LoaderEntityListener extends ConfigurableProcessorEntityListener<DataLoader<?>, DataLoaderType, Loader> {

}
