package edu.tamu.scholars.middleware.config.model;

import edu.tamu.scholars.middleware.discovery.component.Harvester;
import edu.tamu.scholars.middleware.discovery.component.jena.TriplestoreHarvester;

/**
 * {@link MiddlewareConfig} configuration to specify list of harvesters.
 * 
 * <p>See `middleware.harvesters` in src/main/resources/application.yml.</p>
 */
public class HarvesterConfig extends IndexDocumentTypesConfig {
    
    private Class<? extends Harvester> type = TriplestoreHarvester.class;

    public HarvesterConfig() {
        super();
    }

    public Class<? extends Harvester> getType() {
        return type;
    }

    public void setType(Class<? extends Harvester> type) {
        this.type = type;
    }

}
