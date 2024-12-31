package edu.tamu.scholars.discovery.etl.model.repo;

import org.springframework.data.repository.NoRepositoryBean;

import edu.tamu.scholars.discovery.etl.DataProcessor;
import edu.tamu.scholars.discovery.etl.model.ConfigurableProcessor;
import edu.tamu.scholars.discovery.etl.model.type.DataProcessorType;
import edu.tamu.scholars.discovery.model.repo.NamedRepo;

@NoRepositoryBean
public interface ConfigurableProcessorRepo<P extends DataProcessor, T extends DataProcessorType<P>, C extends ConfigurableProcessor<P, T>>
        extends NamedRepo<C> {

}
