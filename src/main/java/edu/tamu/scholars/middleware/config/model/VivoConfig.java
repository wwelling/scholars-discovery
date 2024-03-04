package edu.tamu.scholars.middleware.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Injectable middleware configuration to specify vivo properties.
 * 
 * <p>See `vivo` in src/main/resources/application.yml.</p>
 */
@Component
@ConfigurationProperties(prefix = "vivo")
public class VivoConfig {

    private String email = "vivo_root@school.edu";

    private String password = "rootPassword";

    private String baseUrl = "http://localhost:8080/vivo";

    private String linkedOpenDataEndpoint = "/individual";

    private String listRdfEndpoint = "/listrdf";

    private String sparqlQueryEndpoint = "/api/sparqlQuery";

    private TriplestoreConfig triplestore;

    public VivoConfig() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLinkedOpenDataEndpoint() {
        return linkedOpenDataEndpoint;
    }

    public String getLinkedOpenDataEndpointUrl() {
        return baseUrl + linkedOpenDataEndpoint;
    }

    public void setLinkedOpenDataEndpoint(String linkedOpenDataEndpoint) {
        this.linkedOpenDataEndpoint = linkedOpenDataEndpoint;
    }

    public String getListRdfEndpoint() {
        return listRdfEndpoint;
    }

    public String getListRdfEndpointUrl() {
        return baseUrl + listRdfEndpoint;
    }

    public void setListRdfEndpoint(String listRdfEndpoint) {
        this.listRdfEndpoint = listRdfEndpoint;
    }

    public String getSparqlQueryEndpoint() {
        return sparqlQueryEndpoint;
    }

    public String getSparqlQueryEndpointUrl() {
        return baseUrl + sparqlQueryEndpoint;
    }

    public void setSparqlQueryEndpoint(String sparqlQueryEndpoint) {
        this.sparqlQueryEndpoint = sparqlQueryEndpoint;
    }

    public TriplestoreConfig getTriplestore() {
        return triplestore;
    }

    public void setTriplestore(TriplestoreConfig triplestore) {
        this.triplestore = triplestore;
    }

}
