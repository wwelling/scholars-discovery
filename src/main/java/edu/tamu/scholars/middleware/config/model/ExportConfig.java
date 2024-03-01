package edu.tamu.scholars.middleware.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Injectable middleware configuration to specify export properties.
 * 
 * <p>See `middleware.export` in src/main/resources/application.yml.</p>
 */
@Component
@ConfigurationProperties(prefix = "middleware.export")
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
