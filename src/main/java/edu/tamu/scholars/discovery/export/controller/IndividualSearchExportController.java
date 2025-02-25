package edu.tamu.scholars.discovery.export.controller;

import static edu.tamu.scholars.discovery.AppConstants.DEFAULT_QUERY;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositorySearchesResource;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import edu.tamu.scholars.discovery.controller.argument.BoostArg;
import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.controller.argument.QueryArg;
import edu.tamu.scholars.discovery.export.argument.ExportArg;
import edu.tamu.scholars.discovery.export.exception.UnknownExporterTypeException;
import edu.tamu.scholars.discovery.export.service.Exporter;
import edu.tamu.scholars.discovery.export.service.ExporterRegistry;
import edu.tamu.scholars.discovery.export.utility.FilenameUtility;
import edu.tamu.scholars.discovery.model.Individual;
import edu.tamu.scholars.discovery.model.repo.IndividualRepo;

@Slf4j
@RestController
public class IndividualSearchExportController implements RepresentationModelProcessor<RepositorySearchesResource> {

    private final IndividualRepo repo;

    private final ExporterRegistry exporterRegistry;

    IndividualSearchExportController(
        @Lazy IndividualRepo repo,
        @Lazy ExporterRegistry exporterRegistry
    ) {
        this.repo = repo;
        this.exporterRegistry = exporterRegistry;
    }

    @GetMapping("/individual/search/export")
    public ResponseEntity<StreamingResponseBody> export(
        @RequestParam(value = "view", required = false, defaultValue = "People") String view,
        @RequestParam(value = "type", required = false, defaultValue = "csv") String type,
        QueryArg query,
        @SortDefault Sort sort,
        List<FilterArg> filters,
        List<BoostArg> boosts,
        List<ExportArg> export
    ) throws UnknownExporterTypeException {
        log.info("/individual/search/export {} {} {} {} {} {} {}", view, type, query, sort, filters, boosts, export);
        Exporter exporter = exporterRegistry.getExporter(type);

        return ResponseEntity.ok()
            .header(CONTENT_DISPOSITION, exporter.contentDisposition(FilenameUtility.normalizeExportFilename(view)))
            .header(CONTENT_TYPE, exporter.contentType())
            .body(exporter.streamIndividuals(repo.export(query, filters, boosts, sort), export));
    }

    @Override
    public RepositorySearchesResource process(RepositorySearchesResource resource) {
        if (Individual.class.equals(resource.getDomainType())) {
            try {
                resource.add(linkTo(methodOn(this.getClass()).export(
                    "People",
                    "csv",
                    QueryArg.of(
                        Optional.of(DEFAULT_QUERY),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                    ),
                    Sort.unsorted(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
                )).withRel("export").withTitle("Discovery export"));
            } catch (UnknownExporterTypeException e) {
                e.printStackTrace();
            }
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
