package edu.tamu.scholars.discovery.config.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.discovery.component.triplestore.TdbTriplestore;

@ExtendWith(MockitoExtension.class)
public class TriplestoreConfigTest {

    @Test
    public void testDefaultConstructor() {
        TriplestoreConfig triplestoreConfig = new TriplestoreConfig();
        assertNotNull(triplestoreConfig);
        assertEquals(TdbTriplestore.class, triplestoreConfig.getType());
        assertEquals("triplestore", triplestoreConfig.getDirectory());
        assertEquals("http://localhost:8080/vivo/api/sparqlQuery", triplestoreConfig.getUrl());
    }

    @Test
    public void testGettersAndSetters() {
        TriplestoreConfig triplestoreConfig = new TriplestoreConfig();
        triplestoreConfig.setType(TdbTriplestore.class);
        assertEquals(TdbTriplestore.class, triplestoreConfig.getType());
        triplestoreConfig.setDirectory("vivo_data");
        assertEquals("vivo_data", triplestoreConfig.getDirectory());
        triplestoreConfig.setUrl("jdbc://localhost:6541/test");
        assertEquals("jdbc://localhost:6541/test", triplestoreConfig.getUrl());
    }

}
