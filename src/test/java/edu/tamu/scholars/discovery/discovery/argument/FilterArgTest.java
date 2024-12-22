package edu.tamu.scholars.discovery.discovery.argument;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.discovery.model.OpKey;

@ExtendWith(MockitoExtension.class)
public class FilterArgTest {

    @Test
    public void testDefaultConstructor() {
        FilterArg filterArg = new FilterArg("class", "Concept", OpKey.EQUALS, "CLAZZ");
        assertNotNull(filterArg);
        assertEquals("class", filterArg.getField());
        assertEquals("Concept", filterArg.getValue());
        assertEquals(OpKey.EQUALS, filterArg.getOpKey());
        assertEquals("{!tag=CLAZZ}class", filterArg.getCommand());
    }

    @Test
    public void testOfQueryParameter() {
        Optional<String> value = Optional.of("Concept");
        Optional<String> opKey = Optional.of(OpKey.EQUALS.getKey());
        Optional<String> tag = Optional.of("CLAZZ");
        FilterArg filterArg = FilterArg.of("class", value, opKey, tag);
        assertNotNull(filterArg);
        assertEquals("class", filterArg.getField());
        assertEquals("Concept", filterArg.getValue());
        assertEquals(OpKey.EQUALS, filterArg.getOpKey());
        assertEquals("{!tag=CLAZZ}class", filterArg.getCommand());
    }

}
