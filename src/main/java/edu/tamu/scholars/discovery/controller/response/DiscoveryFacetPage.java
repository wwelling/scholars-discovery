package edu.tamu.scholars.discovery.controller.response;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.FacetSortArg;
import edu.tamu.scholars.discovery.utility.DateFormatUtility;
import edu.tamu.scholars.discovery.view.model.FacetSort;

public class DiscoveryFacetPage<T> extends DiscoveryPage<T> {

    private static final long serialVersionUID = 8673698977219588493L;

    private final List<Facet> facets;

    public DiscoveryFacetPage(List<T> content, Pageable pageable, long total, List<Facet> facets) {
        super(content, pageable, total);
        this.facets = facets;
    }

    public static <T> DiscoveryFacetPage<T> from(
        List<T> documents,
        JsonNode response,
        Pageable pageable,
        List<FacetArg> facetArguments,
        Class<T> type
    ) {
        List<Facet> facets = buildFacets(response, facetArguments);
        long numFound = response.get("numFound")
            .asLong();

        return new DiscoveryFacetPage<T>(documents, pageable, numFound, facets);
    }

    public static <T> List<Facet> buildFacets(JsonNode response, List<FacetArg> facetArguments) {
        List<Facet> facets = new ArrayList<Facet>();
        // TODO

        return facets;
    }

    private static class FacetEntryComparator implements Comparator<FacetEntry> {

        private final FacetSortArg facetSort;

        private FacetEntryComparator(FacetSortArg facetSort) {
            this.facetSort = facetSort;
        }

        @Override
        public int compare(FacetEntry e1, FacetEntry e2) {
            if (facetSort.getProperty().equals(FacetSort.COUNT)) {
                return facetSort.getDirection().equals(Direction.ASC)
                    ? Long.compare(e1.count, e2.count)
                    : Long.compare(e2.count, e1.count);
            }
            try {
                ZonedDateTime ld1 = DateFormatUtility.parseZonedDateTime(e1.value);
                ZonedDateTime ld2 = DateFormatUtility.parseZonedDateTime(e2.value);
                return facetSort.getDirection().equals(Direction.ASC) ? ld1.compareTo(ld2) : ld2.compareTo(ld1);
            } catch (ParseException pe) {
                if (NumberUtils.isParsable(e1.value) && NumberUtils.isParsable(e2.value)) {
                    Double d1 = Double.parseDouble(e1.value);
                    Double d2 = Double.parseDouble(e2.value);
                    return facetSort.getDirection().equals(Direction.ASC) ? d1.compareTo(d2) : d2.compareTo(d1);
                } else {
                    return facetSort.getDirection().equals(Direction.ASC)
                        ? e1.value.compareTo(e2.value)
                        : e2.value.compareTo(e1.value);
                }
            }
        }

        private static FacetEntryComparator of(FacetSortArg facetSort) {
            return new FacetEntryComparator(facetSort);
        }

    }

    public List<Facet> getFacets() {
        return facets;
    }

    public static class Facet {

        private final String field;

        private final DiscoveryPage<FacetEntry> entries;

        public Facet(String field, DiscoveryPage<FacetEntry> entries) {
            this.field = field;
            this.entries = entries;
        }

        public String getField() {
            return field;
        }

        public DiscoveryPage<FacetEntry> getEntries() {
            return entries;
        }

    }

    public static class FacetEntry {

        private final String value;

        private final long count;

        public FacetEntry(String value, long count) {
            this.value = value;
            this.count = count;
        }

        public String getValue() {
            return value;
        }

        public String getValueKey() {
            try {
                ZonedDateTime date = DateFormatUtility.parseZonedDateTime(value);
                return String.valueOf(date.getYear());
            } catch (ParseException pe) {
                return value;
            }
        }

        public long getCount() {
            return count;
        }

        public static FacetEntry merge(FacetEntry src, FacetEntry dest) {
            return new FacetEntry(src.value, src.count + dest.count);
        }

    }

}
