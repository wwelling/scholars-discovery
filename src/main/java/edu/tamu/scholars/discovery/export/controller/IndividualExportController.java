package edu.tamu.scholars.discovery.export.controller;

import static edu.tamu.scholars.discovery.export.utility.FilenameUtility.normalizeExportFilename;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import edu.tamu.scholars.discovery.controller.assembler.model.IndividualModel;
import edu.tamu.scholars.discovery.export.exception.UnauthorizedExportException;
import edu.tamu.scholars.discovery.export.exception.UnknownExporterTypeException;
import edu.tamu.scholars.discovery.export.service.Exporter;
import edu.tamu.scholars.discovery.export.service.ExporterRegistry;
import edu.tamu.scholars.discovery.model.Individual;
import edu.tamu.scholars.discovery.model.repo.IndividualRepo;

@RestController
public class IndividualExportController implements RepresentationModelProcessor<IndividualModel> {

    private final IndividualRepo repo;

    private final ExporterRegistry exporterRegistry;

    IndividualExportController(
        @Lazy IndividualRepo repo,
        @Lazy ExporterRegistry exporterRegistry
    ) {
        this.repo = repo;
        this.exporterRegistry = exporterRegistry;
    }

    @GetMapping("/individual/{id}/export")
    public ResponseEntity<StreamingResponseBody> export(
        @PathVariable String id,
        @RequestParam(value = "type", required = false, defaultValue = "docx") String type,
        @RequestParam(value = "name", required = true) String name
    ) throws UnknownExporterTypeException, IllegalArgumentException {

        if (type.equals("zip")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(authentication) || !this.isAdmin(authentication)) {
                throw new UnauthorizedExportException("Must be administrator to use zip exporter.");
            }
        }

        Optional<Individual> individual = repo.findById(id);
        if (individual.isPresent()) {
            Individual document = individual.get();
            Exporter exporter = exporterRegistry.getExporter(type);
            return ResponseEntity.ok()
                .header(CONTENT_DISPOSITION, exporter.contentDisposition(normalizeExportFilename(document)))
                .header(CONTENT_TYPE, exporter.contentType())
                .body(exporter.streamIndividual(document, name));
        }
        throw new EntityNotFoundException(String.format("Individual with id %s not found", id));
    }

    @Override
    public IndividualModel process(IndividualModel resource) {
        Individual individual = resource.getContent();
        if (individual != null) {
            if (individual.getProxy().equals("Person")) {
                addResource(resource, new ResourceLink(
                    individual,
                    "docx",
                     "Single Page Bio", 
                     "Individual single page bio export"));
                addResource(resource, new ResourceLink(
                    individual,
                    "docx",
                     "Profile Summary", 
                     "Individual profile summary export"));
                addResource(resource, new ResourceLink(
                    individual,
                    "zip", 
                    "Last 5 Years", 
                    "Individual 5 year publications export"));
                addResource(resource, new ResourceLink(
                    individual,
                    "zip", 
                    "Last 8 Years", 
                    "Individual 8 year publications export"));
            } else if (individual.getProxy().equals("Oragnization")) {
                addResource(resource, new ResourceLink(
                    individual,
                    "zip",
                    "Last 5 Years",
                    "Organization 5 year publications export"));
                addResource(resource, new ResourceLink(
                    individual,
                    "zip",
                    "Last 8 Years",
                    "Organization 8 year publications export"));
            }
        }

        return resource;
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(a -> 
                a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN")
            );
    }

    private void addResource(IndividualModel resource, ResourceLink link) {
        try {
            resource.add(linkTo(methodOn(this.getClass()).export(
                link.getIndividual().getId(),
                link.getType(),
                link.getName()
            )).withRel(link.getName().toLowerCase().replace(" ", "_"))
                .withTitle(link.getTitle()));
        } catch (NullPointerException
            | UnknownExporterTypeException
            | IllegalArgumentException e
        ) {
            e.printStackTrace();
        }
    }

    private class ResourceLink {
        private final Individual individual;
        private final String type;
        private final String name;
        private final String title;

        private ResourceLink(
            Individual individual,
            String type,
            String name,
            String title
        ) {
            this.individual = individual;
            this.type = type;
            this.name = name;
            this.title = title;
        }

        public Individual getIndividual() {
            return individual;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }
        
    }

}
