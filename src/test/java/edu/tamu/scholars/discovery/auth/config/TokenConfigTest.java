package edu.tamu.scholars.discovery.auth.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TokenConfigTest {

    @Test
    public void testDefaultConstructor() {
        TokenConfig tokenConfig = new TokenConfig();
        assertNotNull(tokenConfig);
        assertEquals(1, tokenConfig.getServerInteger());
        assertEquals("wKFkxTX54UzKx6xCYnC8WlEI2wtOy0PR", tokenConfig.getServerSecret());
    }

    @Test
    public void testServerIntegerGetterSetter() {
        TokenConfig tokenConfig = new TokenConfig();
        tokenConfig.setServerInteger(2);
        assertEquals(2, tokenConfig.getServerInteger());
    }

    @Test
    public void testServerSecretGetterSetter() {
        TokenConfig tokenConfig = new TokenConfig();
        tokenConfig.setServerSecret("ulqj2hIqUNbJdTvl1QEeB398XZlhgO3D");
        assertEquals("ulqj2hIqUNbJdTvl1QEeB398XZlhgO3D", tokenConfig.getServerSecret());
    }

    @Test
    public void testPseudoRandomNumberBytesGetterSetter() {
        TokenConfig tokenConfig = new TokenConfig();
        tokenConfig.setPseudoRandomNumberBytes(128);
        assertEquals(128, tokenConfig.getPseudoRandomNumberBytes());
    }

}
