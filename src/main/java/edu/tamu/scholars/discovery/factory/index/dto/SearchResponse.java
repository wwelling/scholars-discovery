package edu.tamu.scholars.discovery.factory.index.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

@Data
@Builder
public class SearchResponse {

    @Builder.Default
    private final long numFound = 0;

    @Builder.Default
    private final Map<String, Map<String, List<String>>> highlighting = new HashMap<>();

    @Builder.Default
    private final Map<String, List<Pair<String, Long>>> faceting = new HashMap<>();

}
