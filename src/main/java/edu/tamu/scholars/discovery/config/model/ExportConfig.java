package edu.tamu.scholars.discovery.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Injectable discovery configuration to specify export properties.
 * 
 * <p>See `discovery.export` in src/main/resources/application.yml.</p>
 */
@Component
@ConfigurationProperties(prefix = "discovery.export")
public class ExportConfig {

    private String individualKey = "individual";

    private String individualBaseUri = "http://localhost:4200/display";

    public String getIndividualKey() {
        return individualKey;
    }

    public void setIndividualKey(String individualKey) {
        this.individualKey = individualKey;
    }

    public String getIndividualBaseUri() {
        return individualBaseUri;
    }

    public void setIndividualBaseUri(String individualBaseUri) {
        this.individualBaseUri = individualBaseUri;
    }

}
