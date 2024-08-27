package edu.tamu.scholars.middleware.discovery.model.repo;

import static edu.tamu.scholars.middleware.discovery.DiscoveryConstants.CLASS;
import static edu.tamu.scholars.middleware.discovery.DiscoveryConstants.DEFAULT_QUERY;
import static edu.tamu.scholars.middleware.discovery.DiscoveryConstants.ID;
import static edu.tamu.scholars.middleware.discovery.DiscoveryConstants.MOD_TIME;
import static edu.tamu.scholars.middleware.discovery.DiscoveryConstants.QUERY_DELIMETER;
import static edu.tamu.scholars.middleware.discovery.DiscoveryConstants.REQUEST_PARAM_DELIMETER;
import static edu.tamu.scholars.middleware.discovery.DiscoveryConstants.TYPE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.StreamingResponseCallback;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.middleware.discovery.argument.BoostArg;
import edu.tamu.scholars.middleware.discovery.argument.DiscoveryAcademicAgeDescriptor;
import edu.tamu.scholars.middleware.discovery.argument.DiscoveryNetworkDescriptor;
import edu.tamu.scholars.middleware.discovery.argument.DiscoveryQuantityDistributionDescriptor;
import edu.tamu.scholars.middleware.discovery.argument.FacetArg;
import edu.tamu.scholars.middleware.discovery.argument.FilterArg;
import edu.tamu.scholars.middleware.discovery.argument.HighlightArg;
import edu.tamu.scholars.middleware.discovery.argument.QueryArg;
import edu.tamu.scholars.middleware.discovery.exception.SolrRequestException;
import edu.tamu.scholars.middleware.discovery.model.Individual;
import edu.tamu.scholars.middleware.discovery.response.DiscoveryAcademicAge;
import edu.tamu.scholars.middleware.discovery.response.DiscoveryFacetAndHighlightPage;
import edu.tamu.scholars.middleware.discovery.response.DiscoveryNetwork;
import edu.tamu.scholars.middleware.discovery.response.DiscoveryQuantityDistribution;
import edu.tamu.scholars.middleware.utility.DateFormatUtility;

/**
 * 
 */
@Service
public class IndividualRepo implements IndexDocumentRepo<Individual> {

    private static final Logger logger = LoggerFactory.getLogger(IndividualRepo.class);

    private static final Pattern RANGE_PATTERN = Pattern.compile("^\\[(.*?) TO (.*?)\\]$");

    @Value("${middleware.index.name}")
    private String collectionName;

    @Value("${spring.data.solr.parser:edismax}")
    private String defType;

    @Value("${spring.data.solr.operator:AND}")
    private String defaultOperator;

    @Lazy
    @Autowired
    private SolrClient solrClient;

    @Override
    public long count(QueryArg query, List<FilterArg> filters) {
        SolrQueryBuilder builder = new SolrQueryBuilder()
            .withQuery(query)
            .withFilters(filters);

        return countQuery(builder.query());
    }

    @Override
    public long count(String query, List<FilterArg> filters) {
        SolrQueryBuilder builder = new SolrQueryBuilder(query)
            .withFilters(filters);

        return countQuery(builder.query());
    }

    @Override
    public Optional<Individual> findById(String id) {
        return Optional.ofNullable(getById(id));
    }

    @Override
    public Page<Individual> findAll(Pageable pageable) {
        SolrQueryBuilder builder = new SolrQueryBuilder()
            .withPage(pageable);

        return findAllQuery(builder.query(), pageable);
    }

    @Override
    public List<Individual> findByType(String type) {
        FilterArg filter = FilterArg.of(TYPE, Optional.of(type), Optional.empty(), Optional.empty());
        SolrQueryBuilder builder = new SolrQueryBuilder()
            .withFilters(Arrays.asList(filter))
            .withRows(Integer.MAX_VALUE);

        return findAllQuery(builder.query());
    }

    @Override
    public List<Individual> findByIdIn(List<String> ids) {
        return findByIdIn(ids, new ArrayList<>(), Sort.unsorted(), Integer.MAX_VALUE);
    }

    @Override
    public List<Individual> findByIdIn(List<String> ids, List<FilterArg> filters, Sort sort, int limit) {
        try {
            SolrQueryBuilder builder = new SolrQueryBuilder()
                .withFilters(filters)
                .withSort(sort)
                .withRows(limit);

            JsonQueryRequest jsonRequest = builder.jsonQuery(ids);

            QueryResponse response = jsonRequest.process(solrClient, collectionName);

            return response.getResults()
                .stream()
                .map(Individual::from)
                .collect(Collectors.toList());
        } catch (IOException | SolrServerException e) {
            throw new SolrRequestException("Failed to find documents from ids", e);
        }
    }

    @Override
    public List<Individual> findMostRecentlyUpdate(Integer limit, List<FilterArg> filters) {
        SolrQueryBuilder builder = new SolrQueryBuilder()
            .withFilters(filters)
            .withSort(Sort.by(Direction.DESC, MOD_TIME))
            .withRows(limit);

        return findAllQuery(builder.query());
    }

    @Override
    public DiscoveryFacetAndHighlightPage<Individual> search(
        QueryArg query,
        List<FacetArg> facets,
        List<FilterArg> filters,
        List<BoostArg> boosts,
        HighlightArg highlight,
        Pageable page
    ) {
        SolrQueryBuilder builder = new SolrQueryBuilder()
            .withQuery(query)
            .withFacets(facets)
            .withFilters(filters)
            .withBoosts(boosts)
            .withHighlight(highlight)
            .withPage(page);

        try {
            QueryResponse response = solrClient.query(collectionName, builder.query());

            List<Individual> individuals = response.getResults()
                .stream()
                .map(Individual::from)
                .collect(Collectors.toList());

            return DiscoveryFacetAndHighlightPage.from(
                individuals,
                response,
                page,
                facets,
                highlight,
                Individual.class
            );
        } catch (IOException | SolrServerException e) {
            throw new SolrRequestException("Failed to search documents", e);
        }
    }

    @Override
    public Flux<Individual> export(QueryArg query, List<FilterArg> filters, List<BoostArg> boosts, Sort sort) {
        SolrQueryBuilder builder = new SolrQueryBuilder()
            .withQuery(query)
            .withFilters(filters)
            .withBoosts(boosts)
            .withSort(sort)
            .withRows(Integer.MAX_VALUE);

        logger.info("{}: Exporting {} {} {} {}", builder.getId(), query, filters, boosts, sort);

        return Flux.create(emitter -> {
            try {
                solrClient.queryAndStreamResponse(collectionName, builder.query(), new StreamingResponseCallback() {
                    private final AtomicLong remaining = new AtomicLong(0);
                    private final AtomicBoolean docListInfoReceived  = new AtomicBoolean(false);

                    @Override
                    public void streamSolrDocument(SolrDocument document) {
                        Individual individual = Individual.from(document);
                        logger.info("{}: streamSolrDocument: {}", builder.getId(), individual);
                        emitter.next(individual);

                        long numRemaining = remaining.decrementAndGet();
                        logger.info("{}: streamSolrDocument remaining: {}", builder.getId(), numRemaining);
                        if (numRemaining == 0 && docListInfoReceived.get()) {
                            logger.info("{}: streamSolrDocument COMPLETE", builder.getId());
                            emitter.complete();
                        }
                    }

                    @Override
                    public void streamDocListInfo(long numFound, long start, Float maxScore) {
                        logger.info("{}: streamDocListInfo {} {} {}", builder.getId(), numFound, start, maxScore);

                        remaining.set(numFound);
                        docListInfoReceived.set(true);

                        if (numFound == 0) {
                            logger.info("{}: streamDocListInfo COMPLETE", builder.getId());
                            emitter.complete();
                        }
                    }

                });
            } catch (IOException | SolrServerException e) {
                throw new SolrRequestException("Failed to stream documents", e);
            }
        });
    }

    @Override
    public DiscoveryNetwork network(DiscoveryNetworkDescriptor dataNetworkDescriptor) {
        final String id = dataNetworkDescriptor.getId();
        final DiscoveryNetwork dataNetwork = DiscoveryNetwork.to(id);

        try {
            final SolrParams queryParams = dataNetworkDescriptor.getSolrParams();

            final QueryResponse response = solrClient.query(collectionName, queryParams);

            final SolrDocumentList documents = response.getResults();

            final String dateField = dataNetworkDescriptor.getDateField();

            for (SolrDocument document : documents) {
                if (document.containsKey(dateField)) {
                    Object dateFieldFromDocument = document.getFieldValue(dateField);
                    validateAndCountDateField(dataNetwork, dateFieldFromDocument);
                }

                List<String> values = getValues(document, dataNetworkDescriptor.getDataFields());

                String iid = (String) document.getFieldValue(ID);

                for (String v1 : values) {
                    dataNetwork.index(v1);

                    if (!v1.endsWith(id)) {
                        dataNetwork.countLink(v1);
                    }
                    for (String v2 : values) {
                        // prefer id as source
                        if (v2.endsWith(id)) {
                            dataNetwork.map(iid, v2, v1);
                        } else {
                            dataNetwork.map(iid, v1, v2);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to build data network!", e);
        }

        return dataNetwork;
    }

    @Override
    public DiscoveryAcademicAge academicAge(
        DiscoveryAcademicAgeDescriptor academicAgeDescriptor,
        QueryArg query,
        List<FilterArg> filters
    ) {
        DiscoveryAcademicAge academicAge =
            new DiscoveryAcademicAge(academicAgeDescriptor.getLabel(), academicAgeDescriptor.getDateField());

        String dateField = academicAgeDescriptor.getDateField();
        String ageField = academicAgeDescriptor.getAgeField();

        try {
            long count = this.count(query, filters);

            String fields = academicAgeDescriptor.getAccumulateMultivaluedDate()
                ? String.format("%s,%s", dateField, ageField)
                : ageField;

            SolrQueryBuilder builder = new SolrQueryBuilder()
                .withQuery(query)
                .withFields(fields)
                .withFilters(filters)
                .withSort(Sort.by(Direction.ASC, ageField))
                .withRows((int) count);

            QueryResponse response = solrClient.query(collectionName, builder.query());

            SolrDocumentList results = response.getResults();

            academicAge.from(academicAgeDescriptor, results);

        } catch (Exception e) {
            logger.error("Failed to gather academic age analytics!", e);
        }
        return academicAge;
    }

    @Override
    public DiscoveryQuantityDistribution quantityDistribution(
        DiscoveryQuantityDistributionDescriptor quantityDistributionDescriptor,
        QueryArg query,
        List<FilterArg> filters
    ) {
        DiscoveryQuantityDistribution quantityDistribution =
            DiscoveryQuantityDistribution.from(quantityDistributionDescriptor);

        String field = quantityDistributionDescriptor.getField();

        List<FacetArg> facets = new ArrayList<FacetArg>() {
            {
                add(FacetArg.of(
                    field, 
                    Optional.of("COUNT,DESC"),
                    Optional.of(String.valueOf(Integer.MAX_VALUE)),
                    Optional.empty(),
                    Optional.of("STRING"),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
                ));
            }
        };

        SolrQueryBuilder builder = new SolrQueryBuilder()
            .withQuery(query)
            .withFacets(facets)
            .withFilters(filters)
            .withRows(0);

        try {
            QueryResponse response = solrClient.query(collectionName, builder.query());

            quantityDistribution.parse(response);
        } catch (IOException | SolrServerException e) {
            throw new SolrRequestException("Failed to lookup distribution", e);
        }

        return quantityDistribution;
    }

    private boolean validateAndCountDateField(DiscoveryNetwork dataNetwork, Object dateFieldFromDocument) {
        String year = null;
        if (dateFieldFromDocument instanceof Date) {
            Date publicationDate = (Date) dateFieldFromDocument;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(publicationDate);
            year = String.valueOf(calendar.get(Calendar.YEAR));
        } else if (dateFieldFromDocument instanceof String) {
            try {
                year = DateFormatUtility.parseYear((String) dateFieldFromDocument);
            } catch (Exception e) {
                logger.warn("Unable to format {}. {}", year, e.getMessage());
                if (logger.isDebugEnabled()) {
                    logger.debug("Unable to format date while building data network", e);
                }
            }
        }

        boolean success = year != null;

        if (success) {
            dataNetwork.countYear(year);
        }

        return success;
    }

    private long countQuery(SolrQuery query) {
        try {
            return solrClient.query(collectionName, query)
                .getResults()
                .getNumFound();
        } catch (IOException | SolrServerException e) {
            throw new SolrRequestException("Failed to count documents", e);
        }
    }

    private Individual getById(String id) {
        try {
            SolrDocument document = solrClient.getById(collectionName, id);

            return Objects.nonNull(document) ? Individual.from(document) : null;
        } catch (IOException | NullPointerException | SolrServerException e) {
            throw new SolrRequestException("Failed to get document by id", e);
        }
    }

    private List<Individual> findAllQuery(SolrQuery query) {
        try {
            return solrClient.query(collectionName, query).getResults()
                .stream()
                .map(Individual::from)
                .collect(Collectors.toList());
        } catch (IOException | SolrServerException e) {
            throw new SolrRequestException("Failed to query documents", e);
        }
    }

    private Page<Individual> findAllQuery(SolrQuery query, Pageable pageable) {
        try {
            SolrDocumentList documents = solrClient.query(collectionName, query)
                .getResults();
            List<Individual> individuals = solrClient.query(collectionName, query)
                .getResults()
                .stream()
                .map(Individual::from)
                .collect(Collectors.toList());

            return new PageImpl<Individual>(individuals, pageable, documents.getNumFound());
        } catch (IOException | SolrServerException e) {
            throw new SolrRequestException("Failed to query documents", e);
        }
    }

    private List<String> getValues(SolrDocument document, List<String> dataFields) {
        return dataFields.stream()
            .filter(document::containsKey)
            .flatMap(v -> document.getFieldValues(v).stream())
            .map(v -> (String) v)
            .collect(Collectors.toList());
    }

    private class SolrQueryBuilder {

        private final UUID id;

        private final SolrQuery query;

        private final List<String> filters;

        private SolrQueryBuilder() {
            this(DEFAULT_QUERY);
        }

        private SolrQueryBuilder(String query) {
            this.id = UUID.randomUUID();
            this.query = new SolrQuery()
                .setParam("defType", defType)
                .setParam("q.op", defaultOperator)
                .setQuery(query);
            this.filters = new ArrayList<>();
        }

        public String getId() {
            return id.toString();
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
                String fields = String.join(REQUEST_PARAM_DELIMETER, ID, CLASS, query.getFields());
                String fl = String.join(
                    REQUEST_PARAM_DELIMETER,
                    Arrays.stream(fields.split(REQUEST_PARAM_DELIMETER)).collect(Collectors.toSet())
                );
                this.query.setParam("fl", fl);
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
            sort.iterator().forEachRemaining(order -> {
                this.query.addSort(order.getProperty(), order.getDirection().isAscending() ? ORDER.asc : ORDER.desc);
            });

            return this;
        }

        /**
         * Overriding fields set with query.
         * 
         * @param fl Solr fl query parameter
         * @return this
         */
        public SolrQueryBuilder withFields(String fl) {
            this.query.setParam("fl", fl);

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
                this.filters.add(filterQuery.toString());
                this.query.addFilterQuery(filterQuery.toString());
            });

            return this;
        }

        public SolrQueryBuilder withFacets(List<FacetArg> facets) {
            facets.forEach(facet -> {
                String name = facet.getCommand();
                switch (facet.getType()) {
                    case NUMBER_RANGE:
                        Integer rangeStart = Integer.parseInt(facet.getRangeStart());
                        Integer rangeEnd = Integer.parseInt(facet.getRangeEnd());
                        Integer rangeGap = Integer.parseInt(facet.getRangeGap());
                        this.query.addNumericRangeFacet(name, rangeStart, rangeEnd, rangeGap);
                        break;
                    default:
                        this.query.addFacetField(name);
                        break;
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
            final String query = this.query.getQuery();
            StringBuilder boostedQuery = new StringBuilder(query);
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
            logger.debug(this.query.toString());
            return this.query;
        }

        public JsonQueryRequest jsonQuery(List<String> ids) {
            final ModifiableSolrParams params = new ModifiableSolrParams();
            params.set("q.op", defaultOperator);

            JsonQueryRequest request = new JsonQueryRequest(params)
                .setQuery(DEFAULT_QUERY)
                .setLimit(this.query.getRows());

            if (ids.size() == 1) {
                request.withFilter(String.format("id:%s", ids.get(0)));
            } else if (ids.size() > 1) {
                // ¯\_(ツ)_/¯
                ids.add(0, ids.get(0));
                request.withFilter(String.format("{!terms f=id}:%s", String.join(",", ids)));
            }

            // there is variation of boolean logic for filtering
            // for export filters grouped by field are OR'd together
            for (String filter : this.filters) {
                request.withFilter(filter.replace(" AND ", " OR "));
            }

            String sort = this.query.getSorts().stream()
                .map(s -> String.format("%s %s", s.getItem(), s.getOrder().toString()))
                .collect(Collectors.joining(","));

            request.setSort(sort);

            return request;
        }

    }

    public class FilterQueryBuilder {

        private final FilterArg filter;

        private final boolean skipTag;

        public FilterQueryBuilder(FilterArg filter) {
            this.filter = filter;
            this.skipTag = false;
        }

        public FilterQueryBuilder(FilterArg filter, boolean skipTag) {
            this.filter = filter;
            this.skipTag = skipTag;
        }

        public String build() {
            String field = skipTag ? filter.getField() : filter.getCommand();
            String value = filter.getValue();

            StringBuilder filterQuery = new StringBuilder()
                .append(field)
                .append(QUERY_DELIMETER);

            switch (filter.getOpKey()) {
                case BETWEEN:
                    Matcher rangeMatcher = RANGE_PATTERN.matcher(value);
                    if (rangeMatcher.matches()) {
                        String start = rangeMatcher.group(1);
                        String end = rangeMatcher.group(2);

                        // NOTE: hard coded inclusive start exclusive end
                        filterQuery
                            .append("[")
                            .append(start)
                            .append(" TO ")
                            .append(end)
                            .append("}");

                        // NOTE: if date field, must be ISO format for Solr to recognize
                        // https://lucene.apache.org/solr/7_5_0/solr-core/org/apache/solr/schema/DatePointField.html
                    } else {
                        filterQuery
                            .append("\"")
                            .append(value)
                            .append("\"");
                    }
                    break;
                case ENDS_WITH:
                    filterQuery
                        .append(value)
                        .append("}");
                    break;
                case EQUALS:
                    filterQuery
                        .append("\"")
                        .append(value)
                        .append("\"");
                    break;
                case FUZZY:
                    // NOTE: only supporting single-word terms and default edit distance of 2
                    filterQuery
                        .append(value)
                        .append("~");
                    break;
                case NOT_EQUALS:
                    filterQuery
                        .append("!")
                        .append("\"")
                        .append(value)
                        .append("\"");
                    break;
                case STARTS_WITH:
                    filterQuery
                        .append("{!edismax qf=")
                        .append(field)
                        .append("}")
                        .append(value)
                        .append("*");
                    break;
                case CONTAINS:
                    throw new UnsupportedOperationException("CONTAINS: Solr contains query not yet supported");
                case EXPRESSION:
                case RAW:
                    filterQuery
                        .append(value);
                    break;
                default:
                    break;
            }

            return filterQuery.toString();
        }

    }

}
