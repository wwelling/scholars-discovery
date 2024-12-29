package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.scholars.discovery.component.Destination;
import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.model.Loader;
import edu.tamu.scholars.discovery.etl.model.type.DataLoaderType;

@RepositoryEventHandler(Loader.class)
public class LoaderHandler extends ConfigurableProcessorHandler<DataLoader<?>, Destination, DataLoaderType, Loader> {

}
