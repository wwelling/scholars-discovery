package edu.tamu.scholars.discovery.view.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
public class DisplaySubectionViewTest {

    @Test
    public void testDefaultConstructor() {
        DisplaySubsectionView subsection = new DisplaySubsectionView();
        assertNotNull(subsection);
        assertNotNull(subsection.getFilters());
        assertNotNull(subsection.getSort());
        assertTrue(subsection.getFilters().isEmpty());
        assertTrue(subsection.getSort().isEmpty());
        assertEquals(5, subsection.getPageSize());
    }

    @Test
    public void testGettersAndSetters() {
        DisplaySubsectionView subsection = new DisplaySubsectionView();

        subsection.setName("Test");
        subsection.setField("publications");

        subsection.setOrder(1);

        Filter filter = new Filter();
        filter.setField("type");
        filter.setValue("Test");

        Set<Filter> filters = new HashSet<Filter>();
        filters.add(filter);

        subsection.setFilters(filters);

        Sort sort = new Sort();
        sort.setField("date");
        sort.setDirection(Direction.DESC);
        sort.setDate(true);

        Set<Sort> sorting = new HashSet<Sort>();
        sorting.add(sort);

        subsection.setSort(sorting);

        subsection.setPageSize(10);

        subsection.setTemplate("<div>Subsection</div>");

        assertEquals("Test", subsection.getName());
        assertEquals("publications", subsection.getField());
        assertEquals(1, subsection.getOrder());

        assertEquals(1, subsection.getFilters().size());
        assertEquals("type", subsection.getFilters().iterator().next().getField());
        assertEquals("Test", subsection.getFilters().iterator().next().getValue());

        assertEquals(1, subsection.getSort().size());
        assertEquals("date", subsection.getSort().iterator().next().getField());
        assertEquals(Direction.DESC, subsection.getSort().iterator().next().getDirection());
        assertTrue(subsection.getSort().iterator().next().isDate());

        assertEquals(10, subsection.getPageSize());

        assertEquals("<div>Subsection</div>", subsection.getTemplate());
    }

}
