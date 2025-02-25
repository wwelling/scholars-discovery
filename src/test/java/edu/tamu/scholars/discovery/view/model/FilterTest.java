package edu.tamu.scholars.discovery.view.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FilterTest {

    @Test
    public void testDefaultConstructor() {
        Filter filter = new Filter();
        assertNotNull(filter);
    }

    @Test
    public void testGettersAndSetters() {
        Filter filter = new Filter();

        filter.setField("test");
        filter.setValue("Hello, Wolrd!");

        assertEquals("test", filter.getField());
        assertEquals("Hello, Wolrd!", filter.getValue());
    }

}
