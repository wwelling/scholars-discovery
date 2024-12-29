package edu.tamu.scholars.discovery.config;

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
    @Scope("prototype")
    Source<?, ?, ?> source(TriplestoreConfig config)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        return construct(config);
    }

    @Bean
    @Scope("prototype")
    Destination index(IndexConfig config)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        return construct(config);
    }

    @Bean
    @Scope("prototype")
    Mapper mapper(MapperConfig config)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        return construct(config);
    }

    private <S extends Service> S construct(ComponentConfig<S> config)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        return config.getType()
                .getConstructor(config.getClass())
                .newInstance(config);
    }

}
