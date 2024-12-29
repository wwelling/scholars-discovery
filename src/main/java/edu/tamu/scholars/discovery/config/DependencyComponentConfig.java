package edu.tamu.scholars.discovery.config;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.lang.reflect.InvocationTargetException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.tamu.scholars.discovery.component.Destination;
import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.component.Service;
import edu.tamu.scholars.discovery.component.Source;
import edu.tamu.scholars.discovery.config.model.ComponentConfig;
import edu.tamu.scholars.discovery.config.model.IndexConfig;
import edu.tamu.scholars.discovery.config.model.MapperConfig;
import edu.tamu.scholars.discovery.config.model.TriplestoreConfig;

@Configuration
public class DependencyComponentConfig {

    @Bean
    @Scope(value = SCOPE_PROTOTYPE)
    Source<?, ?, ?> source(TriplestoreConfig config)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        return construct(config);
    }

    @Bean
    @Scope(value = SCOPE_PROTOTYPE)
    Destination destination(IndexConfig config)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        return construct(config);
    }

    @Bean
    @Scope(value = SCOPE_PROTOTYPE)
    Mapper mapper(MapperConfig config)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        return construct(config);
    }

    // TODO: implement bean creation from data repo if not exists
    /*
    @Bean
    List<Service> services(DataRepo dataRepo, AutowireCapableBeanFactory beanFactory)
        throws InstantiationException, IllegalAccessException, IllegalArgumentException,
        InvocationTargetException, NoSuchMethodException, SecurityException {
        List<Service> services = new ArrayList<>();

        return services;
    }
    */

    private <S extends Service> S construct(ComponentConfig<S> config)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        return config.getType()
            .getConstructor(config.getClass())
            .newInstance(config);
    }

}
