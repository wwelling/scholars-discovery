package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.scholars.discovery.etl.model.DataLoaderType;
import edu.tamu.scholars.discovery.etl.model.Loader;

@RepositoryEventHandler(Loader.class)
public class LoaderHandler extends ConfigurableProcessorHandler<DataLoaderType, Loader> {

}
