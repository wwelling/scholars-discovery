package edu.tamu.scholars.discovery.config.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.discovery.auth.config.AuthConfig;
import edu.tamu.scholars.discovery.auth.config.PasswordConfig;
import edu.tamu.scholars.discovery.auth.config.TokenConfig;

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
        assertEquals("http://localhost:8983/solr", indexConfig.getHost());
        assertEquals("scholars-discovery", indexConfig.getCollection());
        assertEquals("OR", indexConfig.getOperator());
        assertEquals("edismax", indexConfig.getParser());
        assertEquals("America/Chicago", indexConfig.getZone());
        assertEquals(1, indexConfig.getConnectionTimeout());
        assertEquals(5, indexConfig.getIdleTimeout());
        assertEquals(10, indexConfig.getMaxConnectionPerHost());
        assertEquals(5, indexConfig.getRequestTimeout());
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
        newIndexConfig.setHost("http://localhost:8984/solr");
        newIndexConfig.setCollection("discovery");
        newIndexConfig.setOperator("AND");
        newIndexConfig.setParser("desimax");
        newIndexConfig.setZone("America/Chicago");
        newIndexConfig.setConnectionTimeout(2);
        newIndexConfig.setIdleTimeout(10);
        newIndexConfig.setMaxConnectionPerHost(20);
        newIndexConfig.setRequestTimeout(10);
        middlewareConfig.setIndex(newIndexConfig);
        IndexConfig indexConfig = middlewareConfig.getIndex();
        assertEquals("http://localhost:8984/solr", indexConfig.getHost());
        assertEquals("discovery", indexConfig.getCollection());
        assertEquals("AND", indexConfig.getOperator());
        assertEquals("desimax", indexConfig.getParser());
        assertEquals("America/Chicago", indexConfig.getZone());
        assertEquals(2, indexConfig.getConnectionTimeout());
        assertEquals(10, indexConfig.getIdleTimeout());
        assertEquals(20, indexConfig.getMaxConnectionPerHost());
        assertEquals(10, indexConfig.getRequestTimeout());
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
        newTriplestoreConfig.setDirectory("vivo_data");
        middlewareConfig.setTriplestore(newTriplestoreConfig);
        TriplestoreConfig triplestoreConfig = middlewareConfig.getTriplestore();
        triplestoreConfig.setDirectory("vivo_data");
        assertEquals("vivo_data", triplestoreConfig.getDirectory());
    }

}
