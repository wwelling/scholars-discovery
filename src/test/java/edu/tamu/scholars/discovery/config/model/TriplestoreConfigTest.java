package edu.tamu.scholars.discovery.config.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.discovery.service.TdbTriplestore;

@ExtendWith(MockitoExtension.class)
public class TriplestoreConfigTest {

    @Test
    public void testDefaultConstructor() {
        TriplestoreConfig triplestoreConfig = new TriplestoreConfig();
        assertNotNull(triplestoreConfig);
        assertEquals(TdbTriplestore.class, triplestoreConfig.getType());
        assertEquals("triplestore", triplestoreConfig.getDirectory());
        assertNull(triplestoreConfig.getDatasourceUrl());
    }

    @Test
    public void testGettersAndSetters() {
        TriplestoreConfig triplestoreConfig = new TriplestoreConfig();
        triplestoreConfig.setType(TdbTriplestore.class);
        assertEquals(TdbTriplestore.class, triplestoreConfig.getType());
        triplestoreConfig.setDirectory("vivo_data");
        assertEquals("vivo_data", triplestoreConfig.getDirectory());
        triplestoreConfig.setDatasourceUrl("jdbc://localhost:6541/test");
        assertEquals("jdbc://localhost:6541/test", triplestoreConfig.getDatasourceUrl());
    }

}
