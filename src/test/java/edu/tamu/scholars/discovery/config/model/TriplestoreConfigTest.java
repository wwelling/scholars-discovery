package edu.tamu.scholars.discovery.config.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TriplestoreConfigTest {

    @Test
    public void testDefaultConstructor() {
        TriplestoreConfig triplestoreConfig = new TriplestoreConfig();
        assertNotNull(triplestoreConfig);
        assertEquals("triplestore", triplestoreConfig.getDirectory());
    }

    @Test
    public void testGettersAndSetters() {
        TriplestoreConfig triplestoreConfig = new TriplestoreConfig();
        triplestoreConfig.setDirectory("vivo_data");
        assertEquals("vivo_data", triplestoreConfig.getDirectory());
    }

}
