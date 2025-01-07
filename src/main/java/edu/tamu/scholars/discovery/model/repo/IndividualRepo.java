package edu.tamu.scholars.discovery.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
import edu.tamu.scholars.discovery.model.Individual;

@Service
public class IndividualRepo {

    private final Index<?> index;

    IndividualRepo(Index<?> index) {
        this.index = index;
    }

    public Page<Individual> findAll(Pageable page) {
        return this.index.findAll(page);
    }

    public Optional<Individual> findById(String id) {
        return this.index.findById(id);
    }

    public List<Individual> findByIdIn(List<String> ids) {
        return findByIdIn(
            ids,
            List.of(),
            Sort.unsorted(),
            Integer.MAX_VALUE
        );
    }

    public List<Individual> findByIdIn(
        List<String> ids,
        List<FilterArg> filters,
        Sort sort,
        int limit
    ) {
        return this.index.findByIdIn(
            ids,
            filters,
            sort,
            limit
        );
    }

    public List<Individual> findByType(String type) {
        return this.index.findByType(type);
    }

    public List<Individual> findMostRecentlyUpdate(Integer limit, List<FilterArg> filters) {
        return this.index.findMostRecentlyUpdate(limit, filters);
    }

    public long count(String query, List<FilterArg> filters) {
        return this.index.count(query, filters);
    }

    public DiscoveryFacetAndHighlightPage<Individual> search(
            QueryArg query,
            List<FacetArg> facets,
            List<FilterArg> filters,
            List<BoostArg> boosts,
            HighlightArg highlight,
            Pageable page) {
        return this.index.search(
            query,
            facets,
            filters,
            boosts,
            highlight,
            page
        );
    }

    public Flux<Individual> export(
            QueryArg query,
            List<FilterArg> filters,
            List<BoostArg> boosts,
            Sort sort) {
        return this.index.export(
            query,
            filters,
            boosts,
            sort
        );
    }

    public DiscoveryNetwork network(NetworkDescriptorArg networkDescriptorArg) {
        return this.index.network(networkDescriptorArg);
    }

    public DiscoveryAcademicAge academicAge(
            AcademicAgeDescriptorArg academicAgeDescriptorArg,
            QueryArg query,
            List<FilterArg> filters) {
        return this.index.academicAge(
            academicAgeDescriptorArg,
            query,
            filters
        );
    }

    public DiscoveryQuantityDistribution quantityDistribution(
            QuantityDistributionDescriptorArg quantityDistributionDescriptor,
            QueryArg query,
            List<FilterArg> filters) {
        return this.index.quantityDistribution(
            quantityDistributionDescriptor,
            query,
            filters
        );
    }

}
