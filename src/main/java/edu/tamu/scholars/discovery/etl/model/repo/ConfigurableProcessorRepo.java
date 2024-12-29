package edu.tamu.scholars.discovery.etl.model.repo;

import org.springframework.data.repository.NoRepositoryBean;

import edu.tamu.scholars.discovery.component.Service;
import edu.tamu.scholars.discovery.etl.DataProcessor;
import edu.tamu.scholars.discovery.etl.model.ConfigurableProcessor;
import edu.tamu.scholars.discovery.etl.model.type.DataProcessorType;
import edu.tamu.scholars.discovery.model.repo.NamedRepo;

@NoRepositoryBean
public interface ConfigurableProcessorRepo<P extends DataProcessor, S extends Service, T extends DataProcessorType<P, S>, C extends ConfigurableProcessor<P, S, T>>
        extends NamedRepo<C> {

}
