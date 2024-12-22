package edu.tamu.scholars.discovery.theme.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateActiveThemeExceptionTest {

    @Test
    public void testDefaultConstructor() {
        CreateActiveThemeException exception = new CreateActiveThemeException("Test create active theme exception!");
        assertNotNull(exception);
        assertEquals("Test create active theme exception!", exception.getMessage());
    }

    @Test
    public void testThrow() throws CreateActiveThemeException {
        assertThrows(CreateActiveThemeException.class, () -> {
            throw new CreateActiveThemeException("Test create active theme exception!");
        });
    }

}