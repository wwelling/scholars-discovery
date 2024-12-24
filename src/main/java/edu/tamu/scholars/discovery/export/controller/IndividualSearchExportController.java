package edu.tamu.scholars.discovery.export.controller;

import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.DEFAULT_QUERY;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import edu.tamu.scholars.discovery.discovery.argument.BoostArg;
import edu.tamu.scholars.discovery.discovery.argument.FilterArg;
import edu.tamu.scholars.discovery.discovery.argument.QueryArg;
import edu.tamu.scholars.discovery.discovery.model.Individual;
import edu.tamu.scholars.discovery.discovery.model.repo.IndividualRepo;
import edu.tamu.scholars.discovery.export.argument.ExportArg;
import edu.tamu.scholars.discovery.export.exception.UnknownExporterTypeException;
import edu.tamu.scholars.discovery.export.service.Exporter;
import edu.tamu.scholars.discovery.export.service.ExporterRegistry;
import edu.tamu.scholars.discovery.export.utility.FilenameUtility;

@RestController
public class IndividualSearchExportController implements RepresentationModelProcessor<RepositorySearchesResource> {

    private static final Logger logger = LoggerFactory.getLogger(IndividualSearchExportController.class);

    @Lazy
    @Autowired
    private IndividualRepo repo;

    @Lazy
    @Autowired
    private ExporterRegistry exporterRegistry;

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
        logger.info("/individual/search/export {} {} {} {} {} {} {}", view, type, query, sort, filters, boosts, export);
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
