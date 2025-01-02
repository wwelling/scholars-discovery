package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.model.Loader;
import edu.tamu.scholars.discovery.etl.model.type.DataLoaderType;

@Component
@RepositoryEventHandler(Loader.class)
public class LoaderEventHandler extends ConfigurableProcessorEventHandler<DataLoader<?>, DataLoaderType, Loader> {

}
