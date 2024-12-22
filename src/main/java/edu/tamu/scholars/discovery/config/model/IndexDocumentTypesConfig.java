package edu.tamu.scholars.discovery.config.model;

import java.util.ArrayList;
import java.util.List;

import edu.tamu.scholars.discovery.discovery.model.AbstractIndexDocument;

/**
 * Inherited by {@link HarvesterConfig} and {@link IndexerConfig}.
 */
public abstract class IndexDocumentTypesConfig {

    private List<Class<? extends AbstractIndexDocument>> documentTypes = new ArrayList<>();

    IndexDocumentTypesConfig() {
        // this configuration class is instantiated by reflection by Spring
    }

    public List<Class<? extends AbstractIndexDocument>> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(List<Class<? extends AbstractIndexDocument>> documentTypes) {
        this.documentTypes = documentTypes;
    }

}
