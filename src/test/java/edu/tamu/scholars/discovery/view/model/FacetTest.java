package edu.tamu.scholars.discovery.view.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class FacetTest {

    @Test
    public void testDefaultConstructor() {
        Facet facet = new Facet();
        assertNotNull(facet);
        assertEquals(10, facet.getPageSize());
        assertEquals(1, facet.getPageNumber());
        assertTrue(facet.isCollapsed());
        assertFalse(facet.isHidden());
    }

    @Test
    public void testGettersAndSetters() {
        Facet facet = new Facet();

        facet.setName("Test");
        facet.setField("test");
        facet.setType(FacetType.DATE_YEAR);
        facet.setSort(FacetSort.INDEX);
        facet.setDirection(Sort.Direction.ASC);
        facet.setPageSize(5);
        facet.setPageNumber(2);
        facet.setExpandable(true);
        facet.setCollapsible(false);
        facet.setCollapsed(false);
        facet.setUseDialog(false);
        facet.setHidden(true);

        facet.setRangeStart("0");
        facet.setRangeEnd("1000");
        facet.setRangeGap("10");

        assertEquals("Test", facet.getName());
        assertEquals("test", facet.getField());
        assertEquals(FacetType.DATE_YEAR, facet.getType());
        assertEquals(FacetSort.INDEX, facet.getSort());
        assertEquals(Sort.Direction.ASC, facet.getDirection());
        assertEquals(5, facet.getPageSize());
        assertEquals(2, facet.getPageNumber());
        assertTrue(facet.isExpandable());
        assertFalse(facet.isCollapsible());
        assertFalse(facet.isCollapsed());
        assertFalse(facet.isUseDialog());
        assertTrue(facet.isHidden());

        assertEquals("0", facet.getRangeStart());
        assertEquals("1000", facet.getRangeEnd());
        assertEquals("10", facet.getRangeGap());
    }

}
