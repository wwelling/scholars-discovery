package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.scholars.discovery.etl.model.Loader;
import edu.tamu.scholars.discovery.etl.model.type.DataLoaderType;

@RepositoryEventHandler(Loader.class)
public class LoaderHandler extends ConfigurableProcessorHandler<DataLoaderType, Loader> {

}
