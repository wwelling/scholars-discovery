package edu.tamu.scholars.discovery.etl.model.repo;

import org.springframework.data.repository.NoRepositoryBean;

import edu.tamu.scholars.discovery.etl.model.ConfigurableProcessor;
import edu.tamu.scholars.discovery.etl.model.DataProcessorType;
import edu.tamu.scholars.discovery.model.repo.NamedRepo;

@NoRepositoryBean
public interface ConfigurableProcessorRepo<T extends DataProcessorType<?>, P extends ConfigurableProcessor<T>>
        extends NamedRepo<P> {

}
