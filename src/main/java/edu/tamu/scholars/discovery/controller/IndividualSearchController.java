package edu.tamu.scholars.discovery.controller;

import static edu.tamu.scholars.discovery.AppConstants.DEFAULT_QUERY;
import static edu.tamu.scholars.discovery.AppConstants.ID;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositorySearchesResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.scholars.discovery.controller.argument.BoostArg;
import edu.tamu.scholars.discovery.controller.argument.FacetArg;
import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.controller.argument.HighlightArg;
import edu.tamu.scholars.discovery.controller.argument.QueryArg;
import edu.tamu.scholars.discovery.controller.assembler.DiscoveryPagedResourcesAssembler;
import edu.tamu.scholars.discovery.controller.assembler.IndividualResourceAssembler;
import edu.tamu.scholars.discovery.controller.assembler.model.IndividualModel;
import edu.tamu.scholars.discovery.model.Individual;
import edu.tamu.scholars.discovery.model.repo.IndividualRepo;

@RestController
@RequestMapping("/individual")
public class IndividualSearchController implements RepresentationModelProcessor<RepositorySearchesResource> {
 
    private final IndividualRepo repo;

    private final IndividualResourceAssembler assembler;

    private final DiscoveryPagedResourcesAssembler<Individual> pagedAssembler;

    IndividualSearchController(
        @Lazy IndividualRepo repo,
        @Lazy IndividualResourceAssembler assembler,
        @Lazy DiscoveryPagedResourcesAssembler<Individual> pagedAssembler
    ) {
        this.repo = repo;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @GetMapping("/search/findByIdIn")
    public ResponseEntity<CollectionModel<IndividualModel>> findByIdIn(
        @RequestParam(required = true) List<String> ids
    ) {
        return ResponseEntity.ok(assembler.toCollectionModel(repo.findByIdIn(ids)));
    }

    @GetMapping("/search/findByType")
    public ResponseEntity<CollectionModel<IndividualModel>> findByType(@RequestParam(required = true) String type) {
        return ResponseEntity.ok(assembler.toCollectionModel(repo.findByType(type)));
    }

    @GetMapping("/search/recentlyUpdated")
    public ResponseEntity<CollectionModel<IndividualModel>> recentlyUpdated(
        @RequestParam(value = "limit", defaultValue = "10") int limit,
        List<FilterArg> filters
    ) {
        return ResponseEntity.ok(assembler.toCollectionModel(repo.findMostRecentlyUpdate(limit, filters)));
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<PagedModel<IndividualModel>> search(
        QueryArg query,
        List<FacetArg> facets,
        List<FilterArg> filters,
        List<BoostArg> boosts,
        HighlightArg highlight,
        @PageableDefault(page = 0, size = 10, sort = ID, direction = ASC) Pageable page
    ) {
        return ResponseEntity.ok(
            pagedAssembler.toModel(
                repo.search(query, facets, filters, boosts, highlight, page),
                assembler
            )
        );
    }

    @Override
    public RepositorySearchesResource process(RepositorySearchesResource resource) {
        if (Individual.class.equals(resource.getDomainType())) {
            resource.add(linkTo(methodOn(IndividualSearchCountController.class).count(
                DEFAULT_QUERY,
                new ArrayList<>()
            )).withRel("count").withTitle("Count query"));

            resource.add(linkTo(methodOn(this.getClass()).findByIdIn(
                new ArrayList<>()
            )).withRel("findByIdIn").withTitle("Search by ids"));

            resource.add(linkTo(methodOn(this.getClass()).findByType(
                "Person"
            )).withRel("findByType").withTitle("Search by type"));

            resource.add(linkTo(methodOn(this.getClass()).recentlyUpdated(
                10,
                new ArrayList<>()
            )).withRel("recentlyUpdated").withTitle("Recently updated query"));

            resource.add(linkTo(methodOn(this.getClass()).search(
                QueryArg.of(
                    Optional.of(DEFAULT_QUERY),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
                ),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                HighlightArg.of(new String[] {}, Optional.empty(), Optional.empty()),
                PageRequest.of(0, 10)
            )).withRel("advanced").withTitle("Advanced search"));
        }
        return resource;
    }

    class Count {

        private final long value;

        public Count(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }

    }

}
