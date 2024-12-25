package edu.tamu.scholars.discovery.index.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.controller.argument.BoostArg;
import edu.tamu.scholars.discovery.controller.argument.DiscoveryAcademicAgeDescriptor;
import edu.tamu.scholars.discovery.controller.argument.DiscoveryNetworkDescriptor;
import edu.tamu.scholars.discovery.controller.argument.DiscoveryQuantityDistributionDescriptor;
import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.controller.argument.HighlightArg;
import edu.tamu.scholars.discovery.controller.argument.QueryArg;
import edu.tamu.scholars.discovery.controller.response.DiscoveryAcademicAge;
import edu.tamu.scholars.discovery.controller.response.DiscoveryFacetAndHighlightPage;
import edu.tamu.scholars.discovery.controller.response.DiscoveryNetwork;
import edu.tamu.scholars.discovery.controller.response.DiscoveryQuantityDistribution;
import edu.tamu.scholars.discovery.index.model.AbstractIndexDocument;

public interface IndexDocumentRepo<D extends AbstractIndexDocument> {

    public long count(QueryArg query, List<FilterArg> filters);

    public long count(String query, List<FilterArg> filters);

    public Optional<D> findById(String id);

    public Page<D> findAll(Pageable pageable);

    public List<D> findByType(String type);

    public List<D> findByIdIn(List<String> ids);

    public List<D> findByIdIn(
        List<String> ids,
        List<FilterArg> filters,
        Sort sort,
        int limit
    );

    public List<D> findMostRecentlyUpdate(
        Integer limit,
        List<FilterArg> filters
    );

    public DiscoveryFacetAndHighlightPage<D> search(
        QueryArg query,
        List<FacetArg> facets,
        List<FilterArg> filters,
        List<BoostArg> boosts,
        HighlightArg highlight,
        Pageable page
    );

    public Flux<D> export(
        QueryArg query,
        List<FilterArg> filters,
        List<BoostArg> boosts,
        Sort sort
    );

    public DiscoveryNetwork network(
        DiscoveryNetworkDescriptor dataNetworkDescriptor
    );

    public DiscoveryAcademicAge academicAge(
        DiscoveryAcademicAgeDescriptor academicAgeDescriptor,
        QueryArg query,
        List<FilterArg> filters
    );

    public DiscoveryQuantityDistribution quantityDistribution(
        DiscoveryQuantityDistributionDescriptor quantityDistributionDescriptor,
        QueryArg query,
        List<FilterArg> filters
    );

}
