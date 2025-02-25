package edu.tamu.scholars.discovery.controller;

import static edu.tamu.scholars.discovery.AppConstants.ID;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.scholars.discovery.controller.argument.NetworkDescriptorArg;
import edu.tamu.scholars.discovery.controller.assembler.DiscoveryPagedResourcesAssembler;
import edu.tamu.scholars.discovery.controller.assembler.IndividualResourceAssembler;
import edu.tamu.scholars.discovery.controller.assembler.model.IndividualModel;
import edu.tamu.scholars.discovery.controller.response.DiscoveryNetwork;
import edu.tamu.scholars.discovery.model.Individual;
import edu.tamu.scholars.discovery.model.repo.IndividualRepo;

@RestController
public class IndividualController implements RepresentationModelProcessor<IndividualModel> {

    private final IndividualRepo repo;

    private final IndividualResourceAssembler assembler;

    private final DiscoveryPagedResourcesAssembler<Individual> pagedAssembler;

    IndividualController(
        @Lazy IndividualRepo repo,
        @Lazy IndividualResourceAssembler assembler,
        @Lazy DiscoveryPagedResourcesAssembler<Individual> pagedAssembler
    ) {
        this.repo = repo;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @PostMapping("/individual")
    public ResponseEntity<Void> create() {
        return notAllowed();
    }

    @PutMapping("/individual/{id}")
    public ResponseEntity<Void> update(@PathVariable String id) {
        return notAllowed();
    }

    @PatchMapping("/individual/{id}")
    public ResponseEntity<Void> patch(@PathVariable String id) {
        return notAllowed();
    }

    @DeleteMapping("/individual/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return notAllowed();
    }

    @GetMapping("/individual")
    public ResponseEntity<PagedModel<IndividualModel>> get(
        @PageableDefault(page = 0, size = 10, sort = ID, direction = ASC) Pageable page
    ) {
        return ResponseEntity.ok(pagedAssembler.toModel(repo.findAll(page), assembler));
    }

    @GetMapping("/individual/{id}")
    public ResponseEntity<IndividualModel> individual(@PathVariable String id) {
        Optional<Individual> individual = repo.findById(id);
        if (individual.isPresent()) {
            return ResponseEntity.ok(assembler.toModel(individual.get()));
        }
        throw new EntityNotFoundException(String.format("Individual with id %s not found", id));
    }

    @GetMapping("/individual/{id}/network")
    public ResponseEntity<DiscoveryNetwork> network(
        @PathVariable String id,
        @RequestParam(name = "dateField", defaultValue = "publicationDate") String dateField,
        @RequestParam(name = "dataFields", defaultValue = "authors") List<String> dataFields,
        @RequestParam(name = "typeFilter", defaultValue = "class:Document") String typeFilter
    ) {
        // TODO: add argument resolver for NetworkDescriptorArg
        return ResponseEntity.ok(repo.network(NetworkDescriptorArg.of(id, dateField, dataFields, typeFilter)));
    }

    @Override
    public IndividualModel process(IndividualModel resource) {
        try {
            resource.add(linkTo(methodOn(this.getClass()).network(
                resource.getContent().getId(),
                "publicationDate",
                Arrays.asList("authors"),
                "class:Document"
            )).withRel("network").withTitle("Individual discovery network"));
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return resource;
    }

    private ResponseEntity<Void> notAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

}
