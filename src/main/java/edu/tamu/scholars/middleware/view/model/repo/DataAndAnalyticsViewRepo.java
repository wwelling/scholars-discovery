package edu.tamu.scholars.middleware.view.model.repo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.tamu.scholars.middleware.view.model.DataAndAnalyticsView;

/**
 * {@link DataAndAnalyticsView} <a href="https://docs.spring.io/spring-data/rest/reference/repository-resources.html">Sprint Data REST repository resource.</a>
 */
@RepositoryRestResource
public interface DataAndAnalyticsViewRepo extends ViewRepo<DataAndAnalyticsView> {

}
