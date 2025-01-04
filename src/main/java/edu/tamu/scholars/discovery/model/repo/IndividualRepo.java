package edu.tamu.scholars.discovery.model.repo;

import java.util.ArrayList;
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

    public Page<Individual> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    public Optional<Individual> findById(String id) {
        throw new UnsupportedOperationException();
    }

    public List<Individual> findByIdIn(List<String> ids) {
        return findByIdIn(ids, new ArrayList<>(), Sort.unsorted(), Integer.MAX_VALUE);
    }

    public List<Individual> findByIdIn(
            List<String> ids,
            List<FilterArg> filters,
            Sort sort,
            int limit) {
        throw new UnsupportedOperationException();
    }

    public List<Individual> findByType(String type) {
        throw new UnsupportedOperationException();
    }

    public List<Individual> findMostRecentlyUpdate(Integer limit, List<FilterArg> filters) {
        throw new UnsupportedOperationException();
    }

    public long count(String query, List<FilterArg> filters) {
        throw new UnsupportedOperationException();
    }

    public DiscoveryFacetAndHighlightPage<Individual> search(
            QueryArg query,
            List<FacetArg> facets,
            List<FilterArg> filters,
            List<BoostArg> boosts,
            HighlightArg highlight,
            Pageable page) {
        throw new UnsupportedOperationException();
    }

    public Flux<Individual> export(
            QueryArg query,
            List<FilterArg> filters,
            List<BoostArg> boosts,
            Sort sort) {
        throw new UnsupportedOperationException();
    }

    public DiscoveryNetwork network(NetworkDescriptorArg dataNetworkDescriptor) {
        throw new UnsupportedOperationException();
    }

    public DiscoveryAcademicAge academicAge(
            AcademicAgeDescriptorArg academicAgeDescriptorArg,
            QueryArg query,
            List<FilterArg> filters) {
        throw new UnsupportedOperationException();
    }

    public DiscoveryQuantityDistribution quantityDistribution(
            QuantityDistributionDescriptorArg quantityDistributionDescriptor,
            QueryArg query,
            List<FilterArg> filters) {
        throw new UnsupportedOperationException();
    }

}
