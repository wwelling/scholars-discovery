package edu.tamu.scholars.discovery.controller.argument;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort.Direction;

import edu.tamu.scholars.discovery.view.model.FacetSort;
import edu.tamu.scholars.discovery.view.model.FacetType;

@ExtendWith(MockitoExtension.class)
public class FacetArgTest {

    @Test
    public void testDefaultConstructor() {
        FacetArg facetArg = new FacetArg("class", "{}", "COUNT,DESC", 10, 1, "STRING", "CLAZZ", "0", "1000", "10");
        assertNotNull(facetArg);
        assertEquals("class", facetArg.getField());
        assertEquals("{}", facetArg.getDomain());
        assertEquals(FacetSort.COUNT, facetArg.getSort().getProperty());
        assertEquals(Direction.DESC, facetArg.getSort().getDirection());
        assertEquals(10, facetArg.getPageSize());
        assertEquals(1, facetArg.getPageNumber());
        assertEquals(FacetType.STRING, facetArg.getType());
        assertEquals("{!ex=CLAZZ}class", facetArg.getCommand());
        assertEquals("0", facetArg.getRangeStart());
        assertEquals("1000", facetArg.getRangeEnd());
        assertEquals("10", facetArg.getRangeGap());
    }

    @Test
    public void testOfQueryParameter() {
        Optional<String> domain = Optional.empty();
        Optional<String> sort = Optional.of("COUNT,DESC");
        Optional<String> pageSize = Optional.of("10");
        Optional<String> pageNumber = Optional.of("1");
        Optional<String> type = Optional.of("STRING");
        Optional<String> exclusionTag = Optional.of("CLAZZ");
        Optional<String> rangeStart = Optional.of("0");
        Optional<String> rangeEnd = Optional.of("1000");
        Optional<String> rangeGap = Optional.of("10");
        FacetArg facetArg = FacetArg.of("class", domain, sort, pageSize, pageNumber, type, exclusionTag, rangeStart, rangeEnd, rangeGap);
        assertNotNull(facetArg);
        assertEquals("class", facetArg.getField());
        assertEquals("{}", facetArg.getDomain());
        assertEquals(FacetSort.COUNT, facetArg.getSort().getProperty());
        assertEquals(Direction.DESC, facetArg.getSort().getDirection());
        assertEquals(10, facetArg.getPageSize());
        assertEquals(1, facetArg.getPageNumber());
        assertEquals(FacetType.STRING, facetArg.getType());
        assertEquals("{!ex=CLAZZ}class", facetArg.getCommand());
        assertEquals("0", facetArg.getRangeStart());
        assertEquals("1000", facetArg.getRangeEnd());
        assertEquals("10", facetArg.getRangeGap());
    }

}
