package edu.tamu.scholars.discovery.controller.argument;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.discovery.controller.argument.BoostArg;

@ExtendWith(MockitoExtension.class)
public class BoostArgTest {

    @Test
    public void testDefaultConstructor() {
        BoostArg boostArg = new BoostArg("title", 2.0f);
        assertNotNull(boostArg);
        assertEquals("title", boostArg.getField());
        assertEquals(2.0f, boostArg.getValue());
    }

    @Test
    public void testOfQueryParameter() {
        BoostArg boostArg = BoostArg.of("title,2.0");
        assertNotNull(boostArg);
        assertEquals("title", boostArg.getField());
        assertEquals(2.0f, boostArg.getValue());
    }

}
