package edu.tamu.scholars.discovery.controller.response;

import static edu.tamu.scholars.discovery.utility.DiscoveryUtility.findPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Pageable;

import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.HighlightArg;
import edu.tamu.scholars.discovery.index.DiscoveryConstants;

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
        // Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        // if (Objects.nonNull(highlighting)) {
        //     highlighting.entrySet()
        //         .stream()
        //         .filter(DiscoveryFacetAndHighlightPage::hasHighlights)
        //             .forEach(highlightEntry -> {

        //                 String id = highlightEntry.getKey();
        //                 Map<String, List<Object>> snippets = new HashMap<>();

        //                 highlightEntry.getValue().entrySet()
        //                     .stream()
        //                     .filter(DiscoveryFacetAndHighlightPage::hasSnippets)
        //                         .forEach(se -> {

        //                             snippets.put(findPath(se.getKey()), se.getValue().stream().map(s -> {
        //                                 Matcher matcher = REFERENCE_PATTERN.matcher(s);
        //                                 if (matcher.find()) {
        //                                     Map<String, String> value = new HashMap<>();
        //                                     value.put(DiscoveryConstants.ID, matcher.group(2));
        //                                     value.put(DiscoveryConstants.SNIPPET, matcher.group(1) + matcher.group(3));
        //                                     return value;
        //                                 }

        //                                 return s;
        //                             }).collect(Collectors.toList()));
        //                         });

        //                 highlights.add(new Highlight(id, snippets));
        //             });
        // }

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
