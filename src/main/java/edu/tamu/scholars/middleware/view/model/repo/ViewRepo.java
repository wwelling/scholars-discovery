package edu.tamu.scholars.middleware.view.model.repo;

import org.springframework.data.repository.NoRepositoryBean;

import edu.tamu.scholars.middleware.model.repo.NamedRepo;
import edu.tamu.scholars.middleware.view.model.View;

/**
 * {@link View} repository interface to provide generic representation of a {@link NamedRepo}.
 *
 * @param <V> {@link View}
 */
@NoRepositoryBean
public interface ViewRepo<V extends View> extends NamedRepo<V> {

}
