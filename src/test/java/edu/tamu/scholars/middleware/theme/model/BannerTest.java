package edu.tamu.scholars.middleware.theme.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BannerTest {

    @Test
    public void testDefaultConstructor() {
        Banner banner = new Banner();
        assertNotNull(banner);
        assertNotNull(banner.getVariables());
        assertTrue(banner.getVariables().isEmpty());
    }

    @Test
    public void testGettersAndSetters() {
        Banner banner = new Banner();

        banner.setImageUri("/assets/images/banner.png");
        banner.setAltText("Test");

        assertEquals("/assets/images/banner.png", banner.getImageUri());
        assertEquals("Test", banner.getAltText());

        List<Style> bannerVairables = new ArrayList<Style>();
        bannerVairables.add(new Style("--test", "banner"));
        bannerVairables.add(new Style("--variable", "test"));

        banner.setVariables(bannerVairables);

        assertEquals(2, banner.getVariables().size());

        assertEquals("--test", banner.getVariables().get(0).getKey());
        assertEquals("banner", banner.getVariables().get(0).getValue());
        assertEquals("--variable", banner.getVariables().get(1).getKey());
        assertEquals("test", banner.getVariables().get(1).getValue());
    }

}
