package edu.tamu.scholars.discovery.factory.index.solr.builder;

import static edu.tamu.scholars.discovery.AppConstants.CLASS;
import static edu.tamu.scholars.discovery.AppConstants.COLLECTIONS;
import static edu.tamu.scholars.discovery.AppConstants.DEFAULT_QUERY;
import static edu.tamu.scholars.discovery.AppConstants.ID;
import static edu.tamu.scholars.discovery.AppConstants.REQUEST_PARAM_DELIMETER;
import static edu.tamu.scholars.discovery.factory.index.solr.utility.SolrFilterUtility.buildFilter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import edu.tamu.scholars.discovery.controller.argument.BoostArg;
import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.FacetSortArg;
import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.controller.argument.HighlightArg;
import edu.tamu.scholars.discovery.controller.argument.QueryArg;
import edu.tamu.scholars.discovery.view.model.FacetType;

@Slf4j
public class SolrQueryBuilder {

    private final UUID id;

    private final SolrQuery query;

    private SolrQueryBuilder() {
        this(DEFAULT_QUERY);
    }

    private SolrQueryBuilder(String query) {
        this.id = UUID.randomUUID();
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
            String fields = String.join(REQUEST_PARAM_DELIMETER,"[child]", COLLECTIONS, ID, CLASS, query.getFields());
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
                .append(buildFilter(firstOne, false));

            if (filterList.size() > 1) {
                // NOTE: filters grouped by field are AND together
                for (FilterArg arg : filterList.subList(1, filterList.size())) {
                    filterQuery.append(" AND ")
                        .append(buildFilter(arg, true));
                }
            }
            this.query.addFilterQuery(filterQuery.toString());
        });

        return this;
    }

    public SolrQueryBuilder withFacets(List<FacetArg> facetArgs) {
        ObjectNode jsonFacet = JsonNodeFactory.instance.objectNode();

        facetArgs.forEach(facetArg -> {
            String field = facetArg.getField();
            String command = facetArg.getCommand();

            ObjectNode facet = JsonNodeFactory.instance.objectNode();
            ObjectNode sort = JsonNodeFactory.instance.objectNode();

            if (facetArg.getType() == FacetType.NUMBER_RANGE) {
                facet.put("type", "range");
                facet.put("start", Integer.parseInt(facetArg.getRangeStart()));
                facet.put("end", Integer.parseInt(facetArg.getRangeEnd()));
                facet.put("gap", Integer.parseInt(facetArg.getRangeGap()));
            } else {
                facet.put("type", "terms");
            }

            FacetSortArg facetSortArg = facetArg.getSort();

            sort.put(facetSortArg.getProperty().toString().toLowerCase(), facetSortArg.getDirection().toString().toLowerCase());

            facet.put("field", command);
            facet.put("limit", -1);
            facet.put("mincount", 1);

            facet.set("sort", sort);

            facet.putRawValue("domain", new RawValue(facetArg.getDomain()));

            jsonFacet.set(field, facet);
        });

        if (!jsonFacet.isEmpty()) {
            this.query.setParam("json.facet", jsonFacet.toString());
        }

        return this;
    }

    public SolrQueryBuilder withBoosts(List<BoostArg> boosts) {
        StringBuilder boostedQuery = new StringBuilder(this.query.getQuery());
        boosts.forEach(boost -> boostedQuery.append(" OR ")
            .append("(")
            .append(boost.getField())
            .append(":")
            .append("(")
            .append(query)
            .append(")")
            .append("^")
            .append(boost.getValue())
            .append(")"));

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

    public UUID getId() {
        return this.id;
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
