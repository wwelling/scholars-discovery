package edu.tamu.scholars.discovery.etl.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.model.repo.NamedRepo;

@RepositoryRestResource
public interface DataRepo extends NamedRepo<Data> {

    @Override
    @RestResource(exported = false)
    @EntityGraph(value = "Data.Graph", type = EntityGraph.EntityGraphType.FETCH)
    public Optional<Data> findByName(String name);

    @Override
    @RestResource(exported = false)
    @EntityGraph(value = "Data.Graph", type = EntityGraph.EntityGraphType.FETCH)
    public List<Data> findAll();

}
