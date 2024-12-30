package edu.tamu.scholars.discovery.view.model.repo;

import org.springframework.data.repository.NoRepositoryBean;

import edu.tamu.scholars.discovery.model.repo.NamedRepo;
import edu.tamu.scholars.discovery.view.model.View;

@NoRepositoryBean
public interface ViewRepo<V extends View> extends NamedRepo<V> {

}
