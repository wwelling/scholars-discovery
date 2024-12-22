package edu.tamu.scholars.discovery.config;

import java.lang.reflect.InvocationTargetException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.config.model.TriplestoreConfig;
import edu.tamu.scholars.discovery.service.Triplestore;

/**
 * Externally configured triplestore for harvesting.
 * 
 * <p>See {@link TriplestoreConfig}.</p>
 */
@Configuration
public class HarvesterDependencyConfig {

    @Bean
    Triplestore triplestore(MiddlewareConfig discovery)
        throws InstantiationException, IllegalAccessException, IllegalArgumentException,
        InvocationTargetException, NoSuchMethodException, SecurityException {

        return discovery.getTriplestore()
            .getType()
            .getConstructor(TriplestoreConfig.class)
            .newInstance(discovery.getTriplestore());
    }

}
