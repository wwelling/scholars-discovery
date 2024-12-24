package edu.tamu.scholars.discovery.config.model;

import edu.tamu.scholars.discovery.index.component.Indexer;
import edu.tamu.scholars.discovery.index.component.solr.SolrIndexer;

/**
 * {@link MiddlewareConfig} configuration to specify list of indexers.
 * 
 * <p>See `discovery.indexers` in src/main/resources/application.yml.</p>
 */
public class IndexerConfig extends IndexDocumentTypesConfig {

    private Class<? extends Indexer> type = SolrIndexer.class;

    public IndexerConfig() {
        super();
    }

    public Class<? extends Indexer> getType() {
        return type;
    }

    public void setType(Class<? extends Indexer> type) {
        this.type = type;
    }

}
