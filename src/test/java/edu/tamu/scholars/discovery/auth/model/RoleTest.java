package edu.tamu.scholars.discovery.auth.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleTest {

    @Test
    void testGetValue() {
        assertEquals("USER", Role.ROLE_USER.getValue());
        assertEquals("MANAGER", Role.ROLE_MANAGER.getValue());
        assertEquals("ADMIN", Role.ROLE_ADMIN.getValue());
        assertEquals("SUPER_ADMIN", Role.ROLE_SUPER_ADMIN.getValue());
    }

}
