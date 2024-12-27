package edu.tamu.scholars.discovery.etl.model.repo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.model.repo.NamedRepo;

@RepositoryRestResource
public interface DataRepo extends NamedRepo<Data> {

}
