package edu.tamu.scholars.discovery.theme.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LinkTest {

    @Test
    public void testDefaultConstructor() {
        Link link = new Link();
        assertNotNull(link);
    }

    @Test
    public void testGettersAndSetters() {
        Link link = new Link();
        link.setLabel("Home");
        link.setUri("http://localhost:4200");
        assertEquals("Home", link.getLabel());
        assertEquals("http://localhost:4200", link.getUri());
    }

}
