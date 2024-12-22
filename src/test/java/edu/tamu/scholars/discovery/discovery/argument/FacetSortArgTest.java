package edu.tamu.scholars.discovery.discovery.argument;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort.Direction;

import edu.tamu.scholars.discovery.view.model.FacetSort;

@ExtendWith(MockitoExtension.class)
public class FacetSortArgTest {

    @Test
    public void testDefaultConstructor() {
        FacetSortArg facetSortArg = new FacetSortArg("COUNT", "DESC");
        assertNotNull(facetSortArg);
        assertEquals(FacetSort.COUNT, facetSortArg.getProperty());
        assertEquals(Direction.DESC, facetSortArg.getDirection());
    }

    @Test
    public void testOfQueryParameter() {
        FacetSortArg facetSortArg = FacetSortArg.of("COUNT,DESC");
        assertNotNull(facetSortArg);
        assertEquals(FacetSort.COUNT, facetSortArg.getProperty());
        assertEquals(Direction.DESC, facetSortArg.getDirection());
    }

    @Test
    public void testToString() {
        FacetSortArg facetSortArg = FacetSortArg.of("COUNT,DESC");
        assertEquals("COUNT,DESC", facetSortArg.toString());
    }

}
