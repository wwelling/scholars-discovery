package edu.tamu.scholars.discovery.discovery.component.solr;

import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.CLASS;
import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.DEFAULT_QUERY;
import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.ID;
import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.REQUEST_PARAM_DELIMETER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.util.UriComponentsBuilder;

import edu.tamu.scholars.discovery.discovery.argument.BoostArg;
import edu.tamu.scholars.discovery.discovery.argument.FacetArg;
import edu.tamu.scholars.discovery.discovery.argument.FilterArg;
import edu.tamu.scholars.discovery.discovery.argument.HighlightArg;
import edu.tamu.scholars.discovery.discovery.argument.QueryArg;
import edu.tamu.scholars.discovery.view.model.FacetType;

public class SolrQueryBuilder {

    private final UUID id;

    private final UriComponentsBuilder uriBuilder;

    private final List<String> filters;

    private String expression;

    private SolrQueryBuilder(String defType, String defaultOperator) {

        this.id = UUID.randomUUID();

        this.uriBuilder = UriComponentsBuilder.newInstance()
            .queryParam("defType", defType)
            .queryParam("q.op", defaultOperator)
            .queryParam("q", DEFAULT_QUERY);

        this.filters = new ArrayList<>();
    }

    public String getId() {
        return id.toString();
    }

    public SolrQueryBuilder withQuery(QueryArg query) {
        if (StringUtils.isNotEmpty(query.getDefaultField())) {
            this.uriBuilder.queryParam("df", query.getDefaultField());
        }

        if (StringUtils.isNotEmpty(query.getMinimumShouldMatch())) {
            this.uriBuilder.queryParam("mm", query.getMinimumShouldMatch());
        }

        if (StringUtils.isNotEmpty(query.getQueryField())) {
            this.uriBuilder.queryParam("qf", query.getQueryField());
        }

        if (StringUtils.isNotEmpty(query.getBoostQuery())) {
            this.uriBuilder.queryParam("bq", query.getBoostQuery());
        }

        if (StringUtils.isNotEmpty(query.getFields())) {
            String fields = String.join(REQUEST_PARAM_DELIMETER, ID, CLASS, query.getFields());
            String fl = String.join(REQUEST_PARAM_DELIMETER,
                    Arrays.stream(fields.split(REQUEST_PARAM_DELIMETER)).collect(Collectors.toSet()));

            this.uriBuilder.queryParam("fl", fl);
        }

        this.uriBuilder.replaceQueryParam("q", query.getExpression());

        return this;
    }

    public SolrQueryBuilder withQuery(String query) {
        this.uriBuilder.replaceQueryParam("q", query);

        return this;
    }

    public SolrQueryBuilder withPage(Pageable page) {
        return withStart((int) page.getOffset())
            .withRows(page.getPageSize())
            .withSort(page.getSort());
    }

    public SolrQueryBuilder withStart(int start) {
        this.uriBuilder.queryParam("start", start);

        return this;
    }

    public SolrQueryBuilder withRows(int rows) {
        this.uriBuilder.queryParam("rows", rows);

        return this;
    }

    public SolrQueryBuilder withSort(Sort sort) {
        String sortParam = sort.stream()
            .map(order -> order.getProperty() + " " + (order.isAscending() ? "asc" : "desc"))
            .collect(Collectors.joining(","));

        this.uriBuilder.queryParam("sort", sortParam);

        return this;
    }

    public SolrQueryBuilder withFields(String fl) {
        this.uriBuilder.replaceQueryParam("fl", fl);

        return this;
    }

    public SolrQueryBuilder withFilters(List<FilterArg> filters) {
        filters.stream()
            .collect(Collectors.groupingBy(FilterArg::getField))
            .forEach((field, filterList) -> {
                FilterArg firstFilter = filterList.get(0);
                StringBuilder filterQuery = new StringBuilder()
                    .append(SolrFilterQueryBuilder.from(firstFilter, false).build());

                for (FilterArg filter : filterList.subList(1, filterList.size())) {
                    filterQuery.append(" AND ")
                        .append(SolrFilterQueryBuilder.from(filter, true).build());
                }

                this.filters.add(filterQuery.toString());
                this.uriBuilder.queryParam("fq", filterQuery.toString());
            });

        return this;
    }

    public SolrQueryBuilder withFacets(List<FacetArg> facets) {
        facets.forEach(facet -> {
            if (facet.getType() == FacetType.NUMBER_RANGE) {
                this.uriBuilder.queryParam("facet.range", facet.getCommand())
                    .queryParam("facet.range.start", facet.getRangeStart())
                    .queryParam("facet.range.end", facet.getRangeEnd())
                    .queryParam("facet.range.gap", facet.getRangeGap());
            } else {
                this.uriBuilder.queryParam("facet.field", facet.getCommand());
            }
        });
        if (!facets.isEmpty()) {
            this.uriBuilder.queryParam("facet", true)
                .queryParam("facet.limit", -1)
                .queryParam("facet.mincount", 1);
        }

        return this;
    }

    public SolrQueryBuilder withBoosts(List<BoostArg> boosts) {
        String boostedQuery = boosts.stream()
            .map(boost -> String.format("(%s:(%s)^%s)", boost.getField(), expression, boost.getValue()))
            .collect(Collectors.joining(" OR "));

        this.uriBuilder.replaceQueryParam("q", boostedQuery);

        return this;
    }

    public SolrQueryBuilder withHighlight(HighlightArg highlight) {
        if (highlight.getFields().length > 0) {
            this.uriBuilder.queryParam("hl", true)
                .queryParam("hl.fl", String.join(",", highlight.getFields()))
                .queryParam("hl.fragsize", 0);
            if (StringUtils.isNotEmpty(highlight.getPrefix())) {
                this.uriBuilder.queryParam("hl.simple.pre", highlight.getPrefix());
            }
            if (StringUtils.isNotEmpty(highlight.getPostfix())) {
                this.uriBuilder.queryParam("hl.simple.post", highlight.getPostfix());
            }
        }

        return this;
    }

    public String build() {
        return this.uriBuilder.build().toUriString();
    }

    public static SolrQueryBuilder from(String defType, String defaultOperator) {
        return new SolrQueryBuilder(defType, defaultOperator);
    }

}
