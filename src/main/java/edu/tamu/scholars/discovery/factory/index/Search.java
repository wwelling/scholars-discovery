package edu.tamu.scholars.discovery.factory.index;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import edu.tamu.scholars.discovery.model.Individual;

public interface Search {

    Page<Individual> findAll(Pageable page);

    List<Individual> findByType(String type);

    Optional<Individual> findById(String id);

    List<Individual> findByIdIn(
        List<String> ids,
        List<FilterArg> filters,
        Sort sort,
        int limit
    );

    List<Individual> findMostRecentlyUpdate(Integer limit, List<FilterArg> filters);

    long count(String query, List<FilterArg> filters);

    DiscoveryFacetAndHighlightPage search(
        QueryArg query,
        List<FacetArg> facets,
        List<FilterArg> filters,
        List<BoostArg> boosts,
        HighlightArg highlight,
        Pageable page
    );

    Flux<Individual> export(
        QueryArg query,
        List<FilterArg> filters,
        List<BoostArg> boosts,
        Sort sort
    );

    DiscoveryNetwork network(NetworkDescriptorArg dataNetworkDescriptor);

    DiscoveryAcademicAge academicAge(
        AcademicAgeDescriptorArg academicAgeDescriptorArg,
        QueryArg query,
        List<FilterArg> filters
    );

    DiscoveryQuantityDistribution quantityDistribution(
        QuantityDistributionDescriptorArg quantityDistributionDescriptor,
        QueryArg query,
        List<FilterArg> filters
    );

}
