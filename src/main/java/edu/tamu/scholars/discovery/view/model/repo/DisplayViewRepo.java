package edu.tamu.scholars.discovery.view.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import edu.tamu.scholars.discovery.view.model.DisplayView;

@RepositoryRestResource
public interface DisplayViewRepo extends ViewRepo<DisplayView> {

    @EntityGraph(value = "DisplayView.Graph.Export", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<DisplayView> findByTypesIn(List<String> types);

    @Override
    @RestResource(exported = true)
    public Optional<DisplayView> findByName(String name);

}
