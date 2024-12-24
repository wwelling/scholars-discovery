package edu.tamu.scholars.discovery.config;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.tamu.scholars.discovery.config.model.HarvesterConfig;
import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.index.component.Harvester;
import edu.tamu.scholars.discovery.index.model.AbstractIndexDocument;

/**
 * Externally configured list of @{link Harvester} beans.
 * 
 * <p>See {@link MiddlewareConfig}.</p>
 */
@Configuration
public class HarvestingConfig {

    @Bean
    List<Harvester> harvesters(MiddlewareConfig discovery, AutowireCapableBeanFactory beanFactory)
        throws InstantiationException, IllegalAccessException, IllegalArgumentException,
        InvocationTargetException, NoSuchMethodException, SecurityException {
        List<Harvester> harvesters = new ArrayList<>();
        for (HarvesterConfig config : discovery.getHarvesters()) {
            for (Class<? extends AbstractIndexDocument> documentType : config.getDocumentTypes()) {
                harvesters.add(harvester(beanFactory, config.getType(), documentType));
            }
        }
        return harvesters;
    }

    private Harvester harvester(
        AutowireCapableBeanFactory beanFactory,
        Class<? extends Harvester> type,
        Class<? extends AbstractIndexDocument> documentType
    ) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
        InvocationTargetException, NoSuchMethodException, SecurityException {
        Harvester harvester = type.getConstructor(Class.class).newInstance(documentType);
        beanFactory.autowireBean(harvester);
        return harvester;
    }

}
