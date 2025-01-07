package edu.tamu.scholars.discovery.factory.index.solr.builder;

import static edu.tamu.scholars.discovery.AppConstants.CLASS;
import static edu.tamu.scholars.discovery.AppConstants.COLLECTIONS;
import static edu.tamu.scholars.discovery.AppConstants.DEFAULT_QUERY;
import static edu.tamu.scholars.discovery.AppConstants.ID;
import static edu.tamu.scholars.discovery.AppConstants.LABEL;
import static edu.tamu.scholars.discovery.AppConstants.REQUEST_PARAM_DELIMETER;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import edu.tamu.scholars.discovery.controller.argument.BoostArg;
import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.controller.argument.HighlightArg;
import edu.tamu.scholars.discovery.controller.argument.QueryArg;
import edu.tamu.scholars.discovery.view.model.FacetType;

@Slf4j
public class SolrQueryBuilder {

    private final SolrQuery query;

    private SolrQueryBuilder() {
        this(DEFAULT_QUERY);
    }

    private SolrQueryBuilder(String query) {
        this.query = new SolrQuery()
            .setQuery(query)
            .addField("*,[child]");
    }

    public SolrQueryBuilder withQuery(QueryArg query) {
        if (StringUtils.isNotEmpty(query.getDefaultField())) {
            this.query.setParam("df", query.getDefaultField());
        }

        if (StringUtils.isNotEmpty(query.getMinimumShouldMatch())) {
            this.query.setParam("mm", query.getMinimumShouldMatch());
        }

        if (StringUtils.isNotEmpty(query.getQueryField())) {
            this.query.setParam("qf", query.getQueryField());
        }

        if (StringUtils.isNotEmpty(query.getBoostQuery())) {
            this.query.setParam("bq", query.getBoostQuery());
        }

        if (StringUtils.isNotEmpty(query.getFields())) {
            String fields = String.join(REQUEST_PARAM_DELIMETER,"[child]", COLLECTIONS, ID, CLASS, LABEL, query.getFields());
            this.query.setParam("fl", fields);
        }

        this.query.setQuery(query.getExpression());

        return this;
    }

    public SolrQueryBuilder withPage(Pageable page) {
        return withStart((int) page.getOffset())
            .withRows(page.getPageSize())
            .withSort(page.getSort());
    }

    public SolrQueryBuilder withStart(int start) {
        this.query.setStart(start);

        return this;
    }

    public SolrQueryBuilder withRows(int rows) {
        this.query.setRows(rows);

        return this;
    }

    public SolrQueryBuilder withSort(Sort sort) {
        sort.stream()
            .forEach(order -> this.query.addSort(order.getProperty(), getOrder(order.getDirection())));

        return this;
    }

    public SolrQueryBuilder withFilters(List<FilterArg> filters) {
        filters.stream().collect(Collectors.groupingBy(w -> w.getField())).forEach((field, filterList) -> {
            FilterArg firstOne = filterList.get(0);

            StringBuilder filterQuery = new StringBuilder()
                .append(new FilterQueryBuilder(firstOne, false).build());

            if (filterList.size() > 1) {
                // NOTE: filters grouped by field are AND together
                for (FilterArg arg : filterList.subList(1, filterList.size())) {
                    filterQuery.append(" AND ")
                        .append(new FilterQueryBuilder(arg, true).build());
                }
            }
            this.query.addFilterQuery(filterQuery.toString());
        });

        return this;
    }

    public SolrQueryBuilder withFacets(List<FacetArg> facets) {
        facets.forEach(facet -> {
            String name = facet.getCommand();
            if (facet.getType() == FacetType.NUMBER_RANGE) {
                Integer rangeStart = Integer.parseInt(facet.getRangeStart());
                Integer rangeEnd = Integer.parseInt(facet.getRangeEnd());
                Integer rangeGap = Integer.parseInt(facet.getRangeGap());
                this.query.addNumericRangeFacet(name, rangeStart, rangeEnd, rangeGap);
            } else {
                this.query.addFacetField(name);
            }
        });

        if (!facets.isEmpty()) {
            // NOTE: other possible; method, minCount, missing, and prefix
            this.query.setFacet(true);
            this.query.setFacetLimit(-1);
            this.query.setFacetMinCount(1);
        }

        return this;
    }

    public SolrQueryBuilder withBoosts(List<BoostArg> boosts) {
        StringBuilder boostedQuery = new StringBuilder(this.query.getQuery());
        boosts.forEach(boost -> {
            boostedQuery.append(" OR ")
                .append("(")
                .append(boost.getField())
                .append(":")
                .append("(")
                .append(query)
                .append(")")
                .append("^")
                .append(boost.getValue())
                .append(")");
        });

        this.query.setQuery(boostedQuery.toString());

        return this;
    }

    public SolrQueryBuilder withHighlight(HighlightArg highlight) {

        for (String field : highlight.getFields()) {
            this.query.addHighlightField(field);
        }

        if (highlight.getFields().length > 0) {
            this.query.setHighlight(true);
            this.query.setHighlightFragsize(0);
            if (StringUtils.isNotEmpty(highlight.getPrefix())) {
                this.query.setHighlightSimplePre(highlight.getPrefix());
            }
            if (StringUtils.isNotEmpty(highlight.getPostfix())) {
                this.query.setHighlightSimplePost(highlight.getPostfix());
            }
        }

        return this;
    }

    public SolrQuery query() {
        log.debug(this.query.toString());
        return this.query;
    }

    private ORDER getOrder(Direction direction) {
        return direction.isAscending() ? ORDER.asc : ORDER.desc;
    }

    public static SolrQueryBuilder of() {
        return new SolrQueryBuilder();
    }

    public static SolrQueryBuilder of(String query) {
        return new SolrQueryBuilder(query);
    }

    public static SolrQueryBuilder of(Pageable page) {
        return new SolrQueryBuilder()
            .withPage(page);
    }

}
