package edu.tamu.scholars.discovery.config;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.tamu.scholars.discovery.config.model.IndexerConfig;
import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.index.component.Indexer;
import edu.tamu.scholars.discovery.index.model.AbstractIndexDocument;

/**
 * Externally configured list of @{link Indexer} beans.
 */
@Configuration
public class IndexingConfig {

    @Bean
    List<Indexer> indexers(MiddlewareConfig discovery, AutowireCapableBeanFactory beanFactory)
        throws InstantiationException, IllegalAccessException, IllegalArgumentException,
        InvocationTargetException, NoSuchMethodException, SecurityException {
        List<Indexer> indexers = new ArrayList<>();
        for (IndexerConfig config : discovery.getIndexers()) {
            for (Class<? extends AbstractIndexDocument> documentType : config.getDocumentTypes()) {
                indexers.add(indexer(beanFactory, config.getType(), documentType));
            }
        }
        return indexers;
    }

    private Indexer indexer(
        AutowireCapableBeanFactory beanFactory,
        Class<? extends Indexer> type,
        Class<? extends AbstractIndexDocument> documentType
    ) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
        InvocationTargetException, NoSuchMethodException, SecurityException {
        Indexer indexer = type.getConstructor(Class.class).newInstance(documentType);
        beanFactory.autowireBean(indexer);
        return indexer;
    }

}
