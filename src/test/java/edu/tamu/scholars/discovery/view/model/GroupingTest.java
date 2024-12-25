package edu.tamu.scholars.discovery.view.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.discovery.model.OpKey;

@ExtendWith(MockitoExtension.class)
public class GroupingTest {

    @Test
    public void testDefaultConstructor() {
        Grouping grouping = new Grouping();
        assertNotNull(grouping);
    }

    @Test
    public void testGettersAndSetters() {
        Grouping grouping = new Grouping();

        grouping.setField("test");
        grouping.setOpKey(OpKey.ENDS_WITH);

        List<String> options = new ArrayList<String>();
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            options.add(String.valueOf(letter));
        }

        grouping.setOptions(options);

        assertEquals("test", grouping.getField());
        assertEquals(OpKey.ENDS_WITH, grouping.getOpKey());

        assertEquals(26, grouping.getOptions().size());

        int i = 0;
        for (char letter = 'A'; letter <= 'Z'; letter++, i++) {
            assertEquals(String.valueOf(letter), grouping.getOptions().get(i));
        }
    }

}
