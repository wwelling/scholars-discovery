package edu.tamu.scholars.discovery.config.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HttpConfigTest {

    @Test
    public void testDefaultConstructor() {
        HttpConfig httpConfig = new HttpConfig();
        assertNotNull(httpConfig);
        assertEquals(5, httpConfig.getConnectTimeout());
        assertEquals(30, httpConfig.getReadTimeout());
    }

    @Test
    public void testGettersAndSetters() {
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.setConnectTimeout(10);
        httpConfig.setReadTimeout(45);
        assertEquals(10, httpConfig.getConnectTimeout());
        assertEquals(45, httpConfig.getReadTimeout());
    }

}
