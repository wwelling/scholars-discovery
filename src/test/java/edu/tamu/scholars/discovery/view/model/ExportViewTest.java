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
public class ExportViewTest {

    @Test
    public void testDefaultConstructor() {
        ExportView export = new ExportView();
        assertNotNull(export);
        assertEquals(0, export.getLazyReferences().size());
    }

    @Test
    public void testGettersAndSetters() {
        ExportView export = new ExportView();

        export.setName("Test");
        assertEquals("Test", export.getName());

        export.setContentTemplate("<html><body><span>Hello, Content!</span></body></html>");
        assertEquals("<html><body><span>Hello, Content!</span></body></html>", export.getContentTemplate());

        export.setHeaderTemplate("<html><body><span>Hello, Header!</span></body></html>");
        assertEquals("<html><body><span>Hello, Header!</span></body></html>", export.getHeaderTemplate());

        Set<ExportFieldView> fieldViews = new HashSet<ExportFieldView>();

        ExportFieldView exportField = new ExportFieldView();

        exportField.setName("Test");
        exportField.setField("publications");

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

        fieldViews.add(exportField);

        export.setLazyReferences(fieldViews);

        assertEquals(1, export.getLazyReferences().size());

        assertEquals("Test", export.getLazyReferences().iterator().next().getName());
        assertEquals("publications", export.getLazyReferences().iterator().next().getField());

        assertEquals(1, export.getLazyReferences().iterator().next().getFilters().size());
        assertEquals("type", export.getLazyReferences().iterator().next().getFilters().iterator().next().getField());
        assertEquals("Test", export.getLazyReferences().iterator().next().getFilters().iterator().next().getValue());

        assertEquals(1, export.getLazyReferences().iterator().next().getSort().size());
        assertEquals("date", export.getLazyReferences().iterator().next().getSort().iterator().next().getField());
        assertEquals(Direction.DESC, export.getLazyReferences().iterator().next().getSort().iterator().next().getDirection());
        assertTrue(export.getLazyReferences().iterator().next().getSort().iterator().next().isDate());

        assertEquals(10, export.getLazyReferences().iterator().next().getLimit());
    }

}
