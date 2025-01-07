package edu.tamu.scholars.discovery.factory.index.solr;

import static edu.tamu.scholars.discovery.AppConstants.DEFAULT_QUERY;
import static edu.tamu.scholars.discovery.AppConstants.TYPE;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.controller.argument.AcademicAgeDescriptorArg;
import edu.tamu.scholars.discovery.controller.argument.BoostArg;
import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.controller.argument.HighlightArg;
import edu.tamu.scholars.discovery.controller.argument.NetworkDescriptorArg;
import edu.tamu.scholars.discovery.controller.argument.QuantityDistributionDescriptorArg;
import edu.tamu.scholars.discovery.controller.argument.QueryArg;
import edu.tamu.scholars.discovery.controller.response.DiscoveryAcademicAge;
import edu.tamu.scholars.discovery.controller.response.DiscoveryFacetAndHighlightPage;
import edu.tamu.scholars.discovery.controller.response.DiscoveryNetwork;
import edu.tamu.scholars.discovery.controller.response.DiscoveryQuantityDistribution;
import edu.tamu.scholars.discovery.factory.index.Index;
import edu.tamu.scholars.discovery.factory.index.dto.CopyField;
import edu.tamu.scholars.discovery.factory.index.dto.Field;
import edu.tamu.scholars.discovery.factory.index.dto.SearchResponse;
import edu.tamu.scholars.discovery.factory.index.solr.builder.FilterQueryBuilder;
import edu.tamu.scholars.discovery.factory.index.solr.builder.SolrQueryBuilder;
import edu.tamu.scholars.discovery.model.Individual;

@Slf4j
public class ManagedSolrIndex implements Index<SolrInputDocument> {

    private final Http2SolrClient solrClient;

    private ManagedSolrIndex(Http2SolrClient solrClient) {
        this.solrClient = solrClient; 
    }

    @Override
    public List<Field> getFields() {
        SchemaRequest.Fields request = new SchemaRequest.Fields();

        try {
            SchemaResponse.FieldsResponse response = request.process(this.solrClient);

            return response.getFields()
                .stream()
                .map(Field::of)
                .toList();

        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error requesting fields from Solr", e);
        }

        return List.of();
    }

    @Override
    public List<CopyField> getCopyFields() {
        SchemaRequest.CopyFields request = new SchemaRequest.CopyFields();

        try {
            SchemaResponse.CopyFieldsResponse response = request.process(this.solrClient);

            return response.getCopyFields()
                .stream()
                .map(CopyField::of)
                .toList();

        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error requesting copy fields from Solr", e);
        }

        return List.of();
    }

    @Override
    public boolean addFields(List<Field> fields) {
        List<SchemaRequest.Update> addFieldRequests = fields.stream()
            .map(Field::toMap)
            .map(attributes -> (SchemaRequest.Update) new SchemaRequest.AddField(attributes))
            .toList();

        return updateSchema(addFieldRequests);
    }

    @Override
    public boolean addCopyFields(List<CopyField> copyFields) {
        List<SchemaRequest.Update> addCopyFieldRequests = copyFields.stream()
            .map(field -> (SchemaRequest.Update) new SchemaRequest.AddCopyField(field.getSource(), field.getDest()))
            .toList();

        return updateSchema(addCopyFieldRequests);
    }

    @Override
    public Page<Individual> findAll(Pageable page) {
        try {
            SolrQuery query = SolrQueryBuilder.of(page).query();
            SolrDocumentList documents = this.solrClient.query(query)
                .getResults();
            List<Individual> individuals = documents.stream()
                .map(Individual::of)
                .toList();

            return new PageImpl<>(individuals, page, documents.getNumFound());
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error querying Solr collection", e);
        }

        return Page.empty();
    }

    @Override
    public List<Individual> findByType(String type) {
        try {
            FilterArg filter = FilterArg.of(
                TYPE,
                Optional.of(type),
                Optional.empty(),
                Optional.empty()
            );

            SolrQuery query = SolrQueryBuilder.of()
                .withFilters(Arrays.asList(filter))
                .withRows(Integer.MAX_VALUE)
                .query();

            return solrClient.query(query)
                .getResults()
                .stream()
                .map(Individual::of)
                .toList();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error querying Solr collection", e);
        }

        return List.of();
    }

    @Override
    public Optional<Individual> findById(String id) {
        try {
            SolrParams params = new ModifiableSolrParams()
                .add("fl", "*,[child]");

            SolrDocument document = this.solrClient.getById(id, params);

            if (document != null) {
                return Optional.of(Individual.of(document));
            }
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error querying Solr collection", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Individual> findByIdIn(
        List<String> ids,
        List<FilterArg> filters,
        Sort sort,
        int limit
    ) {
        try {
            ModifiableSolrParams params = new ModifiableSolrParams()
                .add("fl", "*,[child]");

            JsonQueryRequest request = new JsonQueryRequest(params)
                .setQuery(DEFAULT_QUERY)
                .setLimit(limit);

            if (ids.size() == 1) {
                request.withFilter(String.format("id:%s", ids.get(0)));
            } else if (ids.size() > 1) {
                request.withFilter(String.format("{!terms f=id}:%s", String.join(",", ids)));
            }

            StringBuilder filtering = new StringBuilder();
            Map<String, List<FilterArg>> filtersByField = filters.stream()
                .collect(Collectors.groupingBy(FilterArg::getField));

            filtersByField.forEach((field, filterList) -> {
                FilterArg filter = filterList.get(0);

                filtering.append(FilterQueryBuilder.of(filter, false).build());

                if (filterList.size() > 1) {
                    for (FilterArg arg : filterList.subList(1, filterList.size())) {
                        filtering.append(" OR ")
                            .append(FilterQueryBuilder.of(arg, true).build());
                    }
                }
            });

            request.withFilter(filtering.toString());

            StringBuilder sorting = new StringBuilder();
            Iterator<Order> orders = sort.iterator();

            while (orders.hasNext()) {
                if (!sorting.isEmpty()) {
                    sorting.append(", ");
                }
                Order order = orders.next();
                sorting.append(order.getProperty())
                    .append(StringUtils.SPACE)
                    .append(order.getDirection().isAscending() ? "asc" : "desc");
            }

            if (!sorting.isEmpty()) {
                request.setSort(sorting.toString());
            }

            QueryResponse response = request.process(this.solrClient);

            return response.getResults()
                .stream()
                .map(Individual::of)
                .toList();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error querying Solr collection", e);
        }

        return List.of();
    }

    @Override
    public List<Individual> findMostRecentlyUpdate(Integer limit, List<FilterArg> filters) {
        try {
            SolrQuery query = SolrQueryBuilder.of()
                .withFilters(filters)
                .withRows(limit)
                .query();

            return solrClient.query(query)
                .getResults()
                .stream()
                .map(Individual::of)
                .toList();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error querying Solr collection", e);
        }

        return List.of();
    }

    @Override
    public long count(String query, List<FilterArg> filters) {
        try {
            SolrQuery solrQuery = SolrQueryBuilder.of(query)
                .withFilters(filters)
                .query();

            return solrClient.query(solrQuery)
                .getResults()
                .getNumFound();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error querying Solr collection", e);
        }

        return 0;
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
        SolrQueryBuilder builder = SolrQueryBuilder.of()
            .withQuery(query)
            .withFacets(facets)
            .withFilters(filters)
            .withBoosts(boosts)
            .withHighlight(highlight)
            .withPage(page);

        try {
            QueryResponse response = solrClient.query(builder.query());

            SolrDocumentList results = response.getResults();

            Map<String, List<Pair<String, Long>>> faceting = response.getFacetFields()
                .stream()
                .collect(Collectors.toMap(
                    FacetField::getName,
                    facetField -> facetField.getValues().stream()
                        .map(count -> Pair.of(count.getName(), count.getCount()))
                    .toList()
            ));

            List<Individual> individuals = results.stream()
                .map(Individual::of)
                .toList();

            return DiscoveryFacetAndHighlightPage.from(
                SearchResponse.builder()
                    .numFound(results.getNumFound())
                    .highlighting(response.getHighlighting())
                    .faceting(faceting)
                    .build(),
                individuals,
                page,
                facets,
                highlight
            );
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error querying Solr collection", e);
        }

        return DiscoveryFacetAndHighlightPage.empty();
    }

    @Override
    public Flux<Individual> export(
        QueryArg query,
        List<FilterArg> filters,
        List<BoostArg> boosts,
        Sort sort
    ) {
        return Flux.empty();
    }

    @Override
    public DiscoveryNetwork network(NetworkDescriptorArg dataNetworkDescriptor) {
        return null;
    }

    @Override
    public DiscoveryAcademicAge academicAge(
        AcademicAgeDescriptorArg academicAgeDescriptorArg,
        QueryArg query,
        List<FilterArg> filters
    ) {
        return null;
    }

    @Override
    public DiscoveryQuantityDistribution quantityDistribution(
        QuantityDistributionDescriptorArg quantityDistributionDescriptor,
        QueryArg query,
        List<FilterArg> filters
    ) {
        return null;
    }

    @Override
    public void update(Collection<SolrInputDocument> documents) {
        try {
            this.solrClient.add(documents);
            this.solrClient.commit();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.warn("Error updating Solr documents", e);
            log.info("Attempting batch documents individually");
            documents.forEach(this::update);
        }
    }

    @Override
    public void update(SolrInputDocument document) {
        try {
            this.solrClient.add(document);
            this.solrClient.commit();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error updating Solr document", e);
        }
    }

    @Override
    public String collection() {
        return this.solrClient.getDefaultCollection();
    }

    @Override
    public int ping() {
        try {
            SolrPingResponse response = this.solrClient.ping();

            return response.getStatus();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error pinging Solr collection", e);
        }

        return -1;
    }

    @Override
    public void optimize() {
        try {
            this.solrClient.optimize();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error optimizing Solr collection", e);
        }
    }

    @Override
    public void close() {
        this.solrClient.close();
    }

    private boolean updateSchema(List<SchemaRequest.Update> updates) {
        SchemaRequest.Update request = new SchemaRequest.MultiUpdate(updates);

        try {
            SchemaResponse.UpdateResponse response = request.process(this.solrClient);

            return response.getStatus() == 0;
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error updating Solr schema", e);
        }

        return false;
    }

    static ManagedSolrIndex with(ManagedSolrIndexConfig config) {
        Http2SolrClient solrClient = new Http2SolrClient.Builder(config.getHost())
            .withConnectionTimeout(config.getConnectionTimeout(), TimeUnit.MINUTES)
            .withIdleTimeout(config.getIdleTimeout(), TimeUnit.MINUTES)
            .withMaxConnectionsPerHost(config.getMaxConnectionPerHost())
            .withRequestTimeout(config.getRequestTimeout(), TimeUnit.MINUTES)
            .withDefaultCollection(config.getCollection())
            .build();

        return new ManagedSolrIndex(solrClient);
    }

}
