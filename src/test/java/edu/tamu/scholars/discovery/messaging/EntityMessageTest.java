package edu.tamu.scholars.discovery.messaging;

import static edu.tamu.scholars.discovery.messaging.EntityAction.CREATE;
import static edu.tamu.scholars.discovery.messaging.EntityAction.DELETE;
import static edu.tamu.scholars.discovery.messaging.EntityAction.UPDATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.discovery.theme.model.Theme;

@ExtendWith(MockitoExtension.class)
public class EntityMessageTest {

    @Test
    public void testCreateEntityMessage() {
        CreateEntityMessage<Theme> createThemeMessage = new CreateEntityMessage<Theme>(new Theme("Test", "Testing Unlimited", "n000001"));
        assertNotNull(createThemeMessage);
        assertEquals("Test", createThemeMessage.getEntity().getName());
        assertEquals("Testing Unlimited", createThemeMessage.getEntity().getOrganization());
        assertEquals("n000001", createThemeMessage.getEntity().getOrganizationId());
        assertEquals(CREATE, createThemeMessage.getAction());
    }

    @Test
    public void testUpdateEntityMessage() {
        UpdateEntityMessage<Theme> updateThemeMessage = new UpdateEntityMessage<Theme>(new Theme("Test", "Testing Unlimited", "n000001"));
        assertNotNull(updateThemeMessage);
        assertEquals("Test", updateThemeMessage.getEntity().getName());
        assertEquals("Testing Unlimited", updateThemeMessage.getEntity().getOrganization());
        assertEquals("n000001", updateThemeMessage.getEntity().getOrganizationId());
        assertEquals(UPDATE, updateThemeMessage.getAction());
    }

    @Test
    public void testDeleteMessage() {
        DeleteEntityMessage<String> deleteThemeMessage = new DeleteEntityMessage<String>("Test");
        assertNotNull(deleteThemeMessage);
        assertEquals("Test", deleteThemeMessage.getIdentifier());
        assertEquals(DELETE, deleteThemeMessage.getAction());
    }

}
