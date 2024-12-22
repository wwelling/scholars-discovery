package edu.tamu.scholars.discovery.config.model;

import edu.tamu.scholars.discovery.service.HttpTriplestore;
import edu.tamu.scholars.discovery.service.TdbTriplestore;
import edu.tamu.scholars.discovery.service.Triplestore;

/**
 * {@link MiddlewareConfig} configuration to specify triplestore.
 * 
 * <p>See `discovery.triplestore` in src/main/resources/application.yml.</p>
 */
public class TriplestoreConfig {

    private Class<? extends Triplestore> type = TdbTriplestore.class;

    /** directory property used by {@link TdbTriplestore } */
    private String directory = "triplestore";

    /** directory property used by {@link HttpTriplestore } */
    private String datasourceUrl;

    public TriplestoreConfig() {
        // this configuration class is instantiated by reflection by Spring
    }

    public Class<? extends Triplestore> getType() {
        return type;
    }

    public void setType(Class<? extends Triplestore> type) {
        this.type = type;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

}
