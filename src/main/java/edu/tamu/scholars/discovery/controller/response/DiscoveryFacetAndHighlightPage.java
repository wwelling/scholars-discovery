package edu.tamu.scholars.discovery.controller.response;

import static edu.tamu.scholars.discovery.AppConstants.ID;
import static edu.tamu.scholars.discovery.AppConstants.SNIPPET;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.HighlightArg;
import edu.tamu.scholars.discovery.factory.index.dto.SearchResponse;

public class DiscoveryFacetAndHighlightPage<T> extends DiscoveryFacetPage<T> {

    private static final long serialVersionUID = 1932430579005159735L;

    private static final Pattern REFERENCE_PATTERN = Pattern.compile("^(.*?)::([\\w\\-\\:]*)(.*)$");

    private final transient List<Highlight> highlights;

    public DiscoveryFacetAndHighlightPage(
        SearchResponse response,
        List<T> content,
        Pageable pageable,
        List<Facet> facets,
        List<Highlight> highlights
    ) {
        super(content, pageable, facets, response.getNumFound());
        this.highlights = highlights;
    }

    public static <T> DiscoveryFacetAndHighlightPage<T> empty() {
        return new DiscoveryFacetAndHighlightPage<>(
            SearchResponse.builder().build(),
            List.of(),
            PageRequest.of(0, 10),
            List.of(),
            List.of()
        );
    }

    public static <T> DiscoveryFacetAndHighlightPage<T> from(
        SearchResponse response,
        List<T> documents,
        Pageable pageable,
        List<FacetArg> facetArguments,
        HighlightArg highlightArg
    ) {
        List<Facet> facets = buildFacets(response, facetArguments);
        List<Highlight> highlights = buildHighlights(response, highlightArg);

        return new DiscoveryFacetAndHighlightPage<>(
            response,
            documents,
            pageable,
            facets,
            highlights
        );
    }

    public static List<Highlight> buildHighlights(SearchResponse response, HighlightArg highlightArg) {
        List<Highlight> highlights = new ArrayList<>();
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        if (Objects.nonNull(highlighting)) {
            highlighting.entrySet()
                .stream()
                .filter(DiscoveryFacetAndHighlightPage::hasHighlights)
                    .forEach(highlightEntry -> {
                        String id = highlightEntry.getKey();
                        Map<String, List<Object>> snippets = new HashMap<>();

                        highlightEntry.getValue().entrySet()
                            .stream()
                            .filter(DiscoveryFacetAndHighlightPage::hasSnippets)
                                .forEach(se -> snippets.put(se.getKey(), se.getValue().stream().map(s -> {
                                    Matcher matcher = REFERENCE_PATTERN.matcher(s);
                                    if (matcher.find()) {
                                        Map<String, String> value = new HashMap<>();
                                        value.put(ID, matcher.group(2));
                                        value.put(SNIPPET, matcher.group(1) + matcher.group(3));
                                        return value;
                                    }

                                    return s;
                                }).toList()));

                        highlights.add(new Highlight(id, snippets));
                    });
        }

        return highlights;
    }

    public static boolean hasHighlights(Entry<String, Map<String, List<String>>> entry) {
        return !entry.getValue().isEmpty();
    }

    public static boolean hasSnippets(Entry<String, List<String>> entry) {
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
