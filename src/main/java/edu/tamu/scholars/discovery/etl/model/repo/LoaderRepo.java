package edu.tamu.scholars.discovery.etl.model.repo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.model.Loader;
import edu.tamu.scholars.discovery.etl.model.type.DataLoaderType;

@RepositoryRestResource
public interface LoaderRepo extends ConfigurableProcessorRepo<DataLoader<?>, DataLoaderType, Loader> {

}
