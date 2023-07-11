package edu.tamu.scholars.middleware.discovery.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import edu.tamu.scholars.middleware.discovery.argument.BoostArg;
import edu.tamu.scholars.middleware.discovery.argument.DiscoveryNetworkDescriptor;
import edu.tamu.scholars.middleware.discovery.argument.DiscoveryQuantityDistributionDescriptor;
import edu.tamu.scholars.middleware.discovery.argument.DiscoveryResearchAgeDescriptor;
import edu.tamu.scholars.middleware.discovery.argument.FacetArg;
import edu.tamu.scholars.middleware.discovery.argument.FilterArg;
import edu.tamu.scholars.middleware.discovery.argument.HighlightArg;
import edu.tamu.scholars.middleware.discovery.argument.QueryArg;
import edu.tamu.scholars.middleware.discovery.model.AbstractIndexDocument;
import edu.tamu.scholars.middleware.discovery.response.DiscoveryFacetAndHighlightPage;
import edu.tamu.scholars.middleware.discovery.response.DiscoveryNetwork;
import edu.tamu.scholars.middleware.discovery.response.DiscoveryQuantityDistribution;
import edu.tamu.scholars.middleware.discovery.response.DiscoveryResearchAge;
import reactor.core.publisher.Flux;

public interface IndexDocumentRepo<D extends AbstractIndexDocument> {

    public long count(QueryArg query, List<FilterArg> filters);

    public long count(String query, List<FilterArg> filters);

    public Optional<D> findById(String id);

    public Page<D> findAll(Pageable pageable);

    public List<D> findByType(String type);

    public List<D> findByIdIn(List<String> ids);

    public List<D> findByIdIn(List<String> ids, List<FilterArg> filters, Sort sort, int limit);

    public List<D> findMostRecentlyUpdate(Integer limit, List<FilterArg> filters);

    public DiscoveryFacetAndHighlightPage<D> search(QueryArg query, List<FacetArg> facets, List<FilterArg> filters, List<BoostArg> boosts, HighlightArg highlight, Pageable page);

    public Flux<D> export(QueryArg query, List<FilterArg> filters, List<BoostArg> boosts, Sort sort);

    public DiscoveryNetwork network(DiscoveryNetworkDescriptor dataNetworkDescriptor);

    public DiscoveryResearchAge researcherAge(DiscoveryResearchAgeDescriptor researcherAgeDescriptor, QueryArg query, List<FilterArg> filters);

    public DiscoveryQuantityDistribution quantityDistribution(DiscoveryQuantityDistributionDescriptor quantityDistributionDescriptor, QueryArg query, List<FilterArg> filters);

}
