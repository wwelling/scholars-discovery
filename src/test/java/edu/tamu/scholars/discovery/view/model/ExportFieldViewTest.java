package edu.tamu.scholars.discovery.view.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
public class ExportFieldViewTest {

    @Test
    public void testDefaultConstructor() {
        ExportFieldView exportField = new ExportFieldView();
        assertNotNull(exportField);
        assertNotNull(exportField.getFilters());
        assertNotNull(exportField.getSort());
        assertTrue(exportField.getFilters().isEmpty());
        assertTrue(exportField.getSort().isEmpty());
        assertEquals(Integer.MAX_VALUE, exportField.getLimit());
    }

    @Test
    public void testGettersAndSetters() {
        ExportFieldView exportField = new ExportFieldView();

        exportField.setName("Test");
        exportField.setField("publications");
        exportField.setOrder(1);

        Filter filter = new Filter();
        filter.setField("type");
        filter.setValue("Test");

        Set<Filter> filters = new HashSet<Filter>();
        filters.add(filter);

        exportField.setFilters(filters);

        Sort sort = new Sort();
        sort.setField("date");
        sort.setDirection(Direction.DESC);
        sort.setDate(true);

        Set<Sort> sorting = new HashSet<Sort>();
        sorting.add(sort);

        exportField.setSort(sorting);

        exportField.setLimit(10);

        assertEquals("Test", exportField.getName());
        assertEquals("publications", exportField.getField());
        assertEquals(1, exportField.getOrder());

        assertEquals(1, exportField.getFilters().size());
        assertEquals("type", exportField.getFilters().iterator().next().getField());
        assertEquals("Test", exportField.getFilters().iterator().next().getValue());

        assertEquals(1, exportField.getSort().size());
        assertEquals("date", exportField.getSort().iterator().next().getField());
        assertEquals(Direction.DESC, exportField.getSort().iterator().next().getDirection());
        assertTrue(exportField.getSort().iterator().next().isDate());

        assertEquals(10, exportField.getLimit());
    }

}
