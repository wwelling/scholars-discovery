package edu.tamu.scholars.discovery.controller.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Pageable;

import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.HighlightArg;

public class DiscoveryFacetAndHighlightPage<T> extends DiscoveryFacetPage<T> {

    private static final long serialVersionUID = 1932430579005159735L;

    private static final Pattern REFERENCE_PATTERN = Pattern.compile("^(.*?)::([\\w\\-\\:]*)(.*)$");

    private final List<Highlight> highlights;

    public DiscoveryFacetAndHighlightPage(
        List<T> content,
        Pageable pageable,
        long total,
        List<Facet> facets,
        List<Highlight> highlights
    ) {
        super(content, pageable, total, facets);
        this.highlights = highlights;
    }

    public static <T> DiscoveryFacetAndHighlightPage<T> from(
        List<T> documents,
        JsonNode response,
        Pageable pageable,
        List<FacetArg> facetArguments,
        HighlightArg highlightArg,
        Class<T> type
    ) {
        List<Facet> facets = buildFacets(response, facetArguments);
        List<Highlight> highlights = buildHighlights(response, highlightArg);
        long numFound = response.get("numFound")
            .asLong();

        return new DiscoveryFacetAndHighlightPage<T>(documents, pageable, numFound, facets, highlights);
    }

    public static <T> List<Highlight> buildHighlights(JsonNode response, HighlightArg highlightArg) {
        List<Highlight> highlights = new ArrayList<>();

        return highlights;
    }

    public static <T> boolean hasHighlights(Entry<String, Map<String, List<String>>> entry) {
        return !entry.getValue().isEmpty();
    }

    public static <T> boolean hasSnippets(Entry<String, List<String>> entry) {
        return !entry.getValue().isEmpty();
    }

    public List<Highlight> getHighlights() {
        return highlights;
    }

    public static class Highlight {

        private final String id;

        private final Map<String, List<Object>> snippets;

        public Highlight(String id, Map<String, List<Object>> snippets) {
            this.id = id;
            this.snippets = snippets;
        }

        public String getId() {
            return id;
        }

        public Map<String, List<Object>> getSnippets() {
            return snippets;
        }

    }

}
