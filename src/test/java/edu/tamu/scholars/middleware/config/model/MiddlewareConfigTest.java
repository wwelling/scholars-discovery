package edu.tamu.scholars.middleware.config.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.middleware.auth.config.AuthConfig;
import edu.tamu.scholars.middleware.auth.config.PasswordConfig;
import edu.tamu.scholars.middleware.auth.config.TokenConfig;
import edu.tamu.scholars.middleware.discovery.model.AbstractIndexDocument;
import edu.tamu.scholars.middleware.discovery.model.Collection;
import edu.tamu.scholars.middleware.discovery.model.Concept;
import edu.tamu.scholars.middleware.discovery.model.Document;
import edu.tamu.scholars.middleware.discovery.model.Organization;
import edu.tamu.scholars.middleware.discovery.model.Person;
import edu.tamu.scholars.middleware.discovery.model.Process;
import edu.tamu.scholars.middleware.discovery.model.Relationship;
import edu.tamu.scholars.middleware.service.TDBTriplestore;

@ExtendWith(MockitoExtension.class)
public class MiddlewareConfigTest {

    @Test
    public void testDefaultConstructor() {
        MiddlewareConfig middlewareConfig = new MiddlewareConfig();
        assertNotNull(middlewareConfig);
        AuthConfig authConfig = middlewareConfig.getAuth();
        assertNotNull(authConfig);
        assertEquals(14, authConfig.getRegistrationTokenDuration());
        PasswordConfig passwordConfig = authConfig.getPassword();
        assertNotNull(passwordConfig);
        assertEquals(180, passwordConfig.getDuration());
        assertEquals(8, passwordConfig.getMinLength());
        assertEquals(64, passwordConfig.getMaxLength());
        TokenConfig tokenConfig = authConfig.getToken();
        assertNotNull(tokenConfig);
        assertEquals(1, tokenConfig.getServerInteger());
        assertEquals("wKFkxTX54UzKx6xCYnC8WlEI2wtOy0PR", tokenConfig.getServerSecret());
        assertEquals(64, tokenConfig.getPseudoRandomNumberBytes());
        MailConfig mailConfig = middlewareConfig.getMail();
        assertNotNull(mailConfig);
        assertEquals("scholarsdiscovery@gmail.com", mailConfig.getFrom());
        assertEquals("scholarsdiscovery@gmail.com", mailConfig.getReplyTo());
        HttpConfig httpConfig = middlewareConfig.getHttp();
        assertNotNull(httpConfig);
        assertEquals(5, httpConfig.getConnectTimeout());
        assertEquals(30, httpConfig.getReadTimeout());
        ExportConfig exportConfig = middlewareConfig.getExport();
        assertNotNull(exportConfig);
        assertEquals("http://localhost:4200/display", exportConfig.getIndividualBaseUri());
        IndexConfig indexConfig = middlewareConfig.getIndex();
        assertNotNull(indexConfig);
        assertEquals("0 0 0 * * SUN", indexConfig.getCron());
        assertEquals("America/Chicago", indexConfig.getZone());
        assertEquals(true, indexConfig.isOnStartup());
        assertEquals(10000, indexConfig.getOnStartupDelay());
        assertEquals(10000, indexConfig.getBatchSize());
    }

    @Test
    public void testAuthGetterSetter() {
        MiddlewareConfig middlewareConfig = new MiddlewareConfig();
        AuthConfig newAuthConfig = new AuthConfig();
        PasswordConfig passwordConfig = new PasswordConfig();
        passwordConfig.setDuration(90);
        passwordConfig.setMinLength(12);
        passwordConfig.setMaxLength(32);
        newAuthConfig.setPassword(passwordConfig);
        TokenConfig tokenConfig = new TokenConfig();
        tokenConfig.setServerInteger(2);
        tokenConfig.setServerSecret("ulqj2hIqUNbJdTvl1QEeB398XZlhgO3D");
        tokenConfig.setPseudoRandomNumberBytes(128);
        newAuthConfig.setToken(tokenConfig);
        middlewareConfig.setAuth(newAuthConfig);
        AuthConfig authConfig = middlewareConfig.getAuth();
        assertNotNull(authConfig);
        assertEquals(90, authConfig.getPassword().getDuration());
        assertEquals(12, authConfig.getPassword().getMinLength());
        assertEquals(32, authConfig.getPassword().getMaxLength());
        assertEquals(2, authConfig.getToken().getServerInteger());
        assertEquals("ulqj2hIqUNbJdTvl1QEeB398XZlhgO3D", authConfig.getToken().getServerSecret());
        assertEquals(128, authConfig.getToken().getPseudoRandomNumberBytes());
    }

    @Test
    public void testMailGetterSetter() {
        MiddlewareConfig middlewareConfig = new MiddlewareConfig();
        MailConfig newMailConfig = new MailConfig();
        newMailConfig.setFrom("bborring@mailinator.com");
        newMailConfig.setReplyTo("eexciting@mailinator.com");
        middlewareConfig.setMail(newMailConfig);
        MailConfig mailConfig = middlewareConfig.getMail();
        assertNotNull(mailConfig);
        assertEquals("bborring@mailinator.com", mailConfig.getFrom());
        assertEquals("eexciting@mailinator.com", mailConfig.getReplyTo());
    }

    @Test
    public void testHttpGetterSetter() {
        MiddlewareConfig middlewareConfig = new MiddlewareConfig();
        HttpConfig newHttpConfig = new HttpConfig();
        newHttpConfig.setConnectTimeout(10);
        newHttpConfig.setReadTimeout(45);
        assertEquals(10, newHttpConfig.getConnectTimeout());
        assertEquals(45, newHttpConfig.getReadTimeout());
        middlewareConfig.setHttp(newHttpConfig);
        HttpConfig httpConfig = middlewareConfig.getHttp();
        assertEquals(10, httpConfig.getConnectTimeout());
        assertEquals(45, httpConfig.getReadTimeout());
    }

    @Test
    public void testIndexGetterSetter() {
        MiddlewareConfig middlewareConfig = new MiddlewareConfig();
        IndexConfig newIndexConfig = new IndexConfig();
        newIndexConfig.setCron("0 0 0 * * MON");
        newIndexConfig.setZone("America/Chicago");
        newIndexConfig.setOnStartup(false);
        newIndexConfig.setOnStartupDelay(1000);
        newIndexConfig.setBatchSize(25000);
        middlewareConfig.setIndex(newIndexConfig);
        IndexConfig indexConfig = middlewareConfig.getIndex();
        assertEquals("0 0 0 * * MON", indexConfig.getCron());
        assertEquals("America/Chicago", indexConfig.getZone());
        assertEquals(false, indexConfig.isOnStartup());
        assertEquals(1000, indexConfig.getOnStartupDelay());
        assertEquals(25000, indexConfig.getBatchSize());
    }

    @Test
    public void testExportGetterSetter() {
        MiddlewareConfig middlewareConfig = new MiddlewareConfig();
        ExportConfig newExportConfig = new ExportConfig();
        newExportConfig.setIndividualKey("link");
        newExportConfig.setIndividualBaseUri("http://localhost:8080/vivo/display");
        middlewareConfig.setExport(newExportConfig);
        ExportConfig exportConfig = middlewareConfig.getExport();
        assertEquals("link", exportConfig.getIndividualKey());
        assertEquals("http://localhost:8080/vivo/display", exportConfig.getIndividualBaseUri());
    }

    @Test
    public void testTriplestoreGetterSetter() {
        MiddlewareConfig middlewareConfig = new MiddlewareConfig();
        TriplestoreConfig newTriplestoreConfig = new TriplestoreConfig();
        newTriplestoreConfig.setType(TDBTriplestore.class);
        newTriplestoreConfig.setDirectory("vivo_data");
        newTriplestoreConfig.setDatasourceUrl("jdbc://localhost:6541/test");
        middlewareConfig.setTriplestore(newTriplestoreConfig);
        TriplestoreConfig triplestoreConfig = middlewareConfig.getTriplestore();
        triplestoreConfig.setType(TDBTriplestore.class);
        assertEquals(TDBTriplestore.class, triplestoreConfig.getType());
        triplestoreConfig.setDirectory("vivo_data");
        assertEquals("vivo_data", triplestoreConfig.getDirectory());
        triplestoreConfig.setDatasourceUrl("jdbc://localhost:6541/test");
        assertEquals("jdbc://localhost:6541/test", triplestoreConfig.getDatasourceUrl());
    }

    @Test
    public void testHarvestersGetterSetter() {
        MiddlewareConfig middlewareConfig = new MiddlewareConfig();
        HarvesterConfig newHarvesterConfig = new HarvesterConfig();
        List<Class<? extends AbstractIndexDocument>> documentTypes = new ArrayList<Class<? extends AbstractIndexDocument>>();
        documentTypes.add(Collection.class);
        documentTypes.add(Concept.class);
        documentTypes.add(Document.class);
        documentTypes.add(Organization.class);
        documentTypes.add(Person.class);
        documentTypes.add(Process.class);
        documentTypes.add(Relationship.class);
        newHarvesterConfig.setDocumentTypes(documentTypes);
        List<HarvesterConfig> harvesterConfigs = new ArrayList<HarvesterConfig>();
        harvesterConfigs.add(newHarvesterConfig);
        middlewareConfig.setHarvesters(harvesterConfigs);
        assertEquals(1, middlewareConfig.getHarvesters().size());
        HarvesterConfig harvesterConfig = middlewareConfig.getHarvesters().get(0);
        assertEquals(7, harvesterConfig.getDocumentTypes().size());
    }

    @Test
    public void testIndexersGetterSetter() {
        MiddlewareConfig middlewareConfig = new MiddlewareConfig();
        IndexerConfig newIndexerConfig = new IndexerConfig();
        List<Class<? extends AbstractIndexDocument>> documentTypes = new ArrayList<Class<? extends AbstractIndexDocument>>();
        documentTypes.add(Collection.class);
        documentTypes.add(Concept.class);
        documentTypes.add(Document.class);
        documentTypes.add(Organization.class);
        documentTypes.add(Person.class);
        documentTypes.add(Process.class);
        documentTypes.add(Relationship.class);
        newIndexerConfig.setDocumentTypes(documentTypes);
        List<IndexerConfig> indexerConfigs = new ArrayList<IndexerConfig>();
        indexerConfigs.add(newIndexerConfig);
        middlewareConfig.setIndexers(indexerConfigs);
        assertEquals(1, middlewareConfig.getIndexers().size());
        IndexerConfig indexerConfig = middlewareConfig.getIndexers().get(0);
        assertEquals(7, indexerConfig.getDocumentTypes().size());
    }

}
