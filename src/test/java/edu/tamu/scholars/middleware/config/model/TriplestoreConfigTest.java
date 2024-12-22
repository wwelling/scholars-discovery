package edu.tamu.scholars.middleware.config.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.middleware.service.TDBTriplestore;

@ExtendWith(MockitoExtension.class)
public class TriplestoreConfigTest {

    @Test
    public void testDefaultConstructor() {
        TriplestoreConfig triplestoreConfig = new TriplestoreConfig();
        assertNotNull(triplestoreConfig);
        assertEquals(TDBTriplestore.class, triplestoreConfig.getType());
        assertEquals("triplestore", triplestoreConfig.getDirectory());
        assertNull(triplestoreConfig.getDatasourceUrl());
    }

    @Test
    public void testGettersAndSetters() {
        TriplestoreConfig triplestoreConfig = new TriplestoreConfig();
        triplestoreConfig.setType(TDBTriplestore.class);
        assertEquals(TDBTriplestore.class, triplestoreConfig.getType());
        triplestoreConfig.setDirectory("vivo_data");
        assertEquals("vivo_data", triplestoreConfig.getDirectory());
        triplestoreConfig.setDatasourceUrl("jdbc://localhost:6541/test");
        assertEquals("jdbc://localhost:6541/test", triplestoreConfig.getDatasourceUrl());
    }

}
