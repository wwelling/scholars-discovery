package edu.tamu.scholars.discovery.index.model.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.controller.argument.BoostArg;
import edu.tamu.scholars.discovery.controller.argument.AcademicAgeDescriptorArg;
import edu.tamu.scholars.discovery.controller.argument.NetworkDescriptorArg;
import edu.tamu.scholars.discovery.controller.argument.QuantityDistributionDescriptorArg;
import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.controller.argument.HighlightArg;
import edu.tamu.scholars.discovery.controller.argument.QueryArg;
import edu.tamu.scholars.discovery.controller.response.DiscoveryAcademicAge;
import edu.tamu.scholars.discovery.controller.response.DiscoveryFacetAndHighlightPage;
import edu.tamu.scholars.discovery.controller.response.DiscoveryNetwork;
import edu.tamu.scholars.discovery.controller.response.DiscoveryQuantityDistribution;
import edu.tamu.scholars.discovery.index.component.solr.SolrService;
import edu.tamu.scholars.discovery.index.model.Individual;

@Service
public class IndividualRepo implements IndexDocumentRepo<Individual> {

    private static final Logger logger = LoggerFactory.getLogger(IndividualRepo.class);

    private final SolrService solrService;

    IndividualRepo(SolrService solrService) {
        this.solrService = solrService;
    }

    @Override
    public long count(QueryArg query, List<FilterArg> filters) {
        return solrService.count(query, filters);
    }

    @Override
    public long count(String query, List<FilterArg> filters) {
        return solrService.count(query, filters);
    }

    @Override
    public Optional<Individual> findById(String id) {
        return solrService.findById(id);
    }

    @Override
    public Page<Individual> findAll(Pageable pageable) {
        return solrService.findAll(pageable);
    }

    @Override
    public List<Individual> findByType(String type) {
        return solrService.findByType(type);
    }

    @Override
    public List<Individual> findByIdIn(List<String> ids) {
        return findByIdIn(ids, new ArrayList<>(), Sort.unsorted(), Integer.MAX_VALUE);
    }

    @Override
    public List<Individual> findByIdIn(List<String> ids, List<FilterArg> filters, Sort sort, int limit) {
        return solrService.findByIdIn(ids, filters, sort, limit);
    }

    @Override
    public List<Individual> findMostRecentlyUpdate(Integer limit, List<FilterArg> filters) {
        return solrService.findMostRecentlyUpdate(limit, filters);
    }

    @Override
    public DiscoveryFacetAndHighlightPage<Individual> search(QueryArg query, List<FacetArg> facets, List<FilterArg> filters, List<BoostArg> boosts, HighlightArg highlight, Pageable page) {
        return solrService.search(query, facets, filters, boosts, highlight, page);
    }

    @Override
    public Flux<Individual> export(QueryArg query, List<FilterArg> filters, List<BoostArg> boosts, Sort sort) {
        // SolrQueryBuilder builder = new SolrQueryBuilder()
        //     .withQuery(query)
        //     .withFilters(filters)
        //     .withBoosts(boosts)
        //     .withSort(sort)
        //     .withRows(Integer.MAX_VALUE);

        // logger.info("{}: Exporting {} {} {} {}", builder.getId(), query, filters, boosts, sort);

        return Flux.create(emitter -> {
            // try {
            //     solrClient.queryAndStreamResponse(collectionName, builder.query(), new StreamingResponseCallback() {
            //         private final AtomicLong remaining = new AtomicLong(0);
            //         private final AtomicBoolean docListInfoReceived  = new AtomicBoolean(false);

            //         @Override
            //         public void streamSolrDocument(SolrDocument document) {
            //             Individual individual = Individual.from(document);
            //             logger.debug("{}: streamSolrDocument: {}", builder.getId(), individual);
            //             emitter.next(individual);

            //             long numRemaining = remaining.decrementAndGet();
            //             logger.debug("{}: streamSolrDocument remaining: {}", builder.getId(), numRemaining);
            //             if (numRemaining == 0 && docListInfoReceived.get()) {
            //                 logger.info("{}: streamSolrDocument COMPLETE", builder.getId());
            //                 emitter.complete();
            //             }
            //         }

            //         @Override
            //         public void streamDocListInfo(long numFound, long start, Float maxScore) {
            //             logger.debug("{}: streamDocListInfo {} {} {}", builder.getId(), numFound, start, maxScore);

            //             remaining.set(numFound);
            //             docListInfoReceived.set(true);

            //             if (numFound == 0) {
            //                 logger.info("{}: streamDocListInfo COMPLETE", builder.getId());
            //                 emitter.complete();
            //             }
            //         }

            //     });
            // } catch (IOException | SolrServerException e) {
            //     throw new SolrRequestException("Failed to stream documents", e);
            // }

            emitter.complete();
        });
    }

    @Override
    public DiscoveryNetwork network(NetworkDescriptorArg networkDescriptor) {
        final String id = networkDescriptor.getId();
        final DiscoveryNetwork dataNetwork = DiscoveryNetwork.to(id);

        try {
            // final SolrParams queryParams = dataNetworkDescriptor.getSolrParams();

            // final QueryResponse response = solrClient.query(collectionName, queryParams);

            // final SolrDocumentList documents = response.getResults();

            // final String dateField = dataNetworkDescriptor.getDateField();

            // for (SolrDocument document : documents) {
            //     if (document.containsKey(dateField)) {
            //         Object dateFieldFromDocument = document.getFieldValue(dateField);
            //         validateAndCountDateField(dataNetwork, dateFieldFromDocument);
            //     }

            //     List<String> values = getValues(document, dataNetworkDescriptor.getDataFields());

            //     String iid = (String) document.getFieldValue(ID);

            //     for (String v1 : values) {
            //         dataNetwork.index(v1);

            //         if (!v1.endsWith(id)) {
            //             dataNetwork.countLink(v1);
            //         }
            //         for (String v2 : values) {
            //             // prefer id as source
            //             if (v2.endsWith(id)) {
            //                 dataNetwork.map(iid, v2, v1);
            //             } else {
            //                 dataNetwork.map(iid, v1, v2);
            //             }
            //         }
            //     }
            // }
        } catch (Exception e) {
            logger.error("Failed to build data network!", e);
        }

        return dataNetwork;
    }

    @Override
    public DiscoveryAcademicAge academicAge(
        AcademicAgeDescriptorArg academicAgeDescriptor,
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

            // SolrQueryBuilder builder = new SolrQueryBuilder()
            //     .withQuery(query)
            //     .withFields(fields)
            //     .withFilters(filters)
            //     .withSort(Sort.by(Direction.ASC, ageField))
            //     .withRows((int) count);

            // QueryResponse response = solrClient.query(collectionName, builder.query());

            // SolrDocumentList results = response.getResults();

            // academicAge.from(academicAgeDescriptor, results);

        } catch (Exception e) {
            logger.error("Failed to gather academic age analytics!", e);
        }
        return academicAge;
    }

    @Override
    public DiscoveryQuantityDistribution quantityDistribution(
        QuantityDistributionDescriptorArg quantityDistributionDescriptor,
        QueryArg query,
        List<FilterArg> filters
    ) {
        DiscoveryQuantityDistribution quantityDistribution =
            DiscoveryQuantityDistribution.from(quantityDistributionDescriptor);

        // String field = quantityDistributionDescriptor.getField();

        // List<FacetArg> facets = new ArrayList<FacetArg>() {
        //     {
        //         add(FacetArg.of(
        //             field, 
        //             Optional.of("COUNT,DESC"),
        //             Optional.of(String.valueOf(Integer.MAX_VALUE)),
        //             Optional.empty(),
        //             Optional.of("STRING"),
        //             Optional.empty(),
        //             Optional.empty(),
        //             Optional.empty(),
        //             Optional.empty()
        //         ));
        //     }
        // };

        // SolrQueryBuilder builder = new SolrQueryBuilder()
        //     .withQuery(query)
        //     .withFacets(facets)
        //     .withFilters(filters)
        //     .withRows(0);

        // try {
        //     QueryResponse response = solrClient.query(collectionName, builder.query());

        //     quantityDistribution.parse(response);
        // } catch (IOException | SolrServerException e) {
        //     throw new SolrRequestException("Failed to lookup distribution", e);
        // }

        return quantityDistribution;
    }

}
