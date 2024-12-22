package edu.tamu.scholars.discovery.view.model.repo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.scholars.discovery.view.model.DiscoveryView;

/**
 * {@link DiscoveryView} <a href="https://docs.spring.io/spring-data/rest/reference/repository-resources.html">Sprint Data REST repository resource.</a>
 */
@RepositoryRestResource
public interface DiscoveryViewRepo extends ViewRepo<DiscoveryView> {

}
