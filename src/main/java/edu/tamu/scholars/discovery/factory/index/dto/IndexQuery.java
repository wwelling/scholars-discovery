package edu.tamu.scholars.discovery.factory.index.dto;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import edu.tamu.scholars.discovery.controller.argument.BoostArg;
import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.controller.argument.HighlightArg;
import edu.tamu.scholars.discovery.controller.argument.QueryArg;

@Data
@Builder
public class IndexQuery {

    private Map<String, String> params;

    private QueryArg query;

    private Pageable page;

    private Sort sort;

    private List<FilterArg> filters;

    private List<FacetArg> facets;

    private List<BoostArg> boosts;

    private HighlightArg highlight;

}
