package edu.tamu.scholars.discovery.factory.index.solr;

import static edu.tamu.scholars.discovery.AppConstants.DEFAULT_QUERY;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.factory.index.Index;
import edu.tamu.scholars.discovery.factory.index.dto.CopyField;
import edu.tamu.scholars.discovery.factory.index.dto.Field;
import edu.tamu.scholars.discovery.factory.index.dto.IndexQuery;
import edu.tamu.scholars.discovery.model.Individual;
import edu.tamu.scholars.discovery.model.repo.builder.FilterQueryBuilder;

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
    public List<Individual> query(IndexQuery query) {
        throw new UnsupportedOperationException("Unimplemented method 'query'");
    }

    @Override
    public Flux<Individual> queryAndStreamResponse(IndexQuery query) {
        throw new UnsupportedOperationException("Unimplemented method 'queryAndStreamResponse'");
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
            log.error("Error pinging Solr collection", e);
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
                    // NOTE: filters grouped by field are AND together
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
            log.error("Error pinging Solr collection", e);
        }

        return List.of();
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
        } catch (SolrServerException | IOException e) {
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
