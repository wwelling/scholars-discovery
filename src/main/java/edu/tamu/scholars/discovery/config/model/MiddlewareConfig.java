package edu.tamu.scholars.discovery.config.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.auth.config.AuthConfig;

/**
 * Injectable discovery configuration.
 * 
 * <p>See `discovery` in src/main/resources/application.yml.</p>
 */
@Component
@ConfigurationProperties(prefix = "discovery")
public class MiddlewareConfig {

    private String assetsLocation = "classpath:assets";

    private boolean loadDefaults = true;

    private boolean updateDefaults = false;

    private List<String> allowedOrigins = new ArrayList<>();

    private AuthConfig auth = new AuthConfig();

    private MailConfig mail = new MailConfig();

    private HttpConfig http = new HttpConfig();

    private IndexConfig index = new IndexConfig();

    private ExportConfig export = new ExportConfig();

    private TriplestoreConfig triplestore = new TriplestoreConfig();

    private List<HarvesterConfig> harvesters = new ArrayList<>();

    private List<IndexerConfig> indexers = new ArrayList<>();

    public MiddlewareConfig() {
        this.allowedOrigins.add("http://localhost:4200");
    }

    public String getAssetsLocation() {
        return assetsLocation;
    }

    public void setAssetsLocation(String assetsLocation) {
        this.assetsLocation = assetsLocation;
    }

    public boolean isLoadDefaults() {
        return loadDefaults;
    }

    public void setLoadDefaults(boolean loadDefaults) {
        this.loadDefaults = loadDefaults;
    }

    public boolean isUpdateDefaults() {
        return updateDefaults;
    }

    public void setUpdateDefaults(boolean updateDefaults) {
        this.updateDefaults = updateDefaults;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public AuthConfig getAuth() {
        return auth;
    }

    public void setAuth(AuthConfig auth) {
        this.auth = auth;
    }

    public MailConfig getMail() {
        return mail;
    }

    public void setMail(MailConfig mail) {
        this.mail = mail;
    }

    public HttpConfig getHttp() {
        return http;
    }

    public void setHttp(HttpConfig http) {
        this.http = http;
    }

    public IndexConfig getIndex() {
        return index;
    }

    public void setIndex(IndexConfig index) {
        this.index = index;
    }

    public ExportConfig getExport() {
        return export;
    }

    public void setExport(ExportConfig export) {
        this.export = export;
    }

    public TriplestoreConfig getTriplestore() {
        return triplestore;
    }

    public void setTriplestore(TriplestoreConfig triplestore) {
        this.triplestore = triplestore;
    }

    public List<HarvesterConfig> getHarvesters() {
        return harvesters;
    }

    public void setHarvesters(List<HarvesterConfig> harvesters) {
        this.harvesters = harvesters;
    }

    public List<IndexerConfig> getIndexers() {
        return indexers;
    }

    public void setIndexers(List<IndexerConfig> indexers) {
        this.indexers = indexers;
    }

}
