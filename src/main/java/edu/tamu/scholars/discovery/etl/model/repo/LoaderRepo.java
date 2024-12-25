package edu.tamu.scholars.discovery.etl.model.repo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.scholars.discovery.etl.model.DataLoaderType;
import edu.tamu.scholars.discovery.etl.model.Loader;

@RepositoryRestResource
public interface LoaderRepo extends ConfigurableProcessorRepo<DataLoaderType, Loader> {

}
