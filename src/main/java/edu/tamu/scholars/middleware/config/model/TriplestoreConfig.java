package edu.tamu.scholars.middleware.config.model;

import edu.tamu.scholars.middleware.service.HttpTriplestore;
import edu.tamu.scholars.middleware.service.TDBTriplestore;
import edu.tamu.scholars.middleware.service.Triplestore;

/**
 * {@link MiddlewareConfig} configuration to specify triplestore.
 * 
 * <p>See `middleware.triplestore` in src/main/resources/application.yml.</p>
 */
public class TriplestoreConfig {

    private Class<? extends Triplestore> type = TDBTriplestore.class;

    /** directory property used by {@link TDBTriplestore } */
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
