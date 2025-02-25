package edu.tamu.scholars.discovery.export.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletContext;
import jakarta.xml.bind.JAXBException;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import edu.tamu.scholars.discovery.export.exception.ExportException;
import edu.tamu.scholars.discovery.export.utility.FilenameUtility;
import edu.tamu.scholars.discovery.export.utility.ZipUtility;
import edu.tamu.scholars.discovery.model.Individual;
import edu.tamu.scholars.discovery.model.repo.IndividualRepo;
import edu.tamu.scholars.discovery.service.TemplateService;
import edu.tamu.scholars.discovery.view.model.DisplayView;
import edu.tamu.scholars.discovery.view.model.ExportFieldView;
import edu.tamu.scholars.discovery.view.model.ExportView;
import edu.tamu.scholars.discovery.view.model.repo.DisplayViewRepo;

@Service
public class ZipDocxExporter extends AbstractDocxExporter {

    private static final String TYPE = "zip";

    private static final String CONTENT_TYPE = "application/zip";

    private static final String CONTENT_DISPOSITION_TEMPLATE = "attachment; filename=%s.zip";

    ZipDocxExporter(
            DisplayViewRepo displayViewRepo,
            IndividualRepo individualRepo,
            TemplateService handlebarsService,
            ObjectMapper mapper,
            ServletContext context) {
        super(displayViewRepo, individualRepo, handlebarsService, mapper, context);
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public String contentDisposition(String filename) {
        return String.format(CONTENT_DISPOSITION_TEMPLATE, filename);
    }

    @Override
    public String contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public StreamingResponseBody streamIndividual(Individual individual, String name) {
        final List<String> types = individual.getType();

        Optional<DisplayView> displayView = displayViewRepo.findByTypesIn(types);

        if (!displayView.isPresent()) {
            throw new ExportException(String.format(
                "Could not find a display view for types: %s", String.join(", ", types))
            );
        }

        Optional<ExportView> exportView = displayView.get()
            .getExportViews()
            .stream()
            .filter(ev -> ev.getName().equalsIgnoreCase(name))
            .findAny();

        if (!exportView.isPresent()) {
            throw new ExportException(String.format(
                "%s display view does not have an export view named %s", displayView.get().getName(), name)
            );
        }

        return outputStream -> {

            final ObjectNode node = mapper.valueToTree(individual);

            Optional<ExportFieldView> multipleReference = Optional.ofNullable(exportView.get().getMultipleReference());

            List<Individual> referenceDocuments = new ArrayList<>();

            if (multipleReference.isPresent()) {
                JsonNode reference = node.get(multipleReference.get().getField());
                List<String> ids = extractIds(reference);
                referenceDocuments.addAll(fetchLazyReference(multipleReference.get(), ids));
            } else {
                referenceDocuments.add(individual);
            }

            File zipFile = File.createTempFile(individual.getId(), ".zip");

            try (
                FileOutputStream fos = new FileOutputStream(zipFile.getAbsolutePath());
                ZipOutputStream zos = new ZipOutputStream(outputStream)
            ) {
                for (Individual refDoc : referenceDocuments) {
                    final ObjectNode refNode = mapper.valueToTree(refDoc);

                    String filename = FilenameUtility.normalizeExportFilename(refDoc);

                    File refDocFile = File.createTempFile(filename, ".docx");

                    try {

                        final WordprocessingMLPackage pkg = createDocx(refNode, exportView.get());

                        pkg.save(refDocFile, Docx4J.FLAG_SAVE_ZIP_FILE);

                        ZipUtility.zipFile(zos, refDocFile);

                    } catch (IOException | JAXBException | Docx4JException e) {
                        throw new ExportException(e.getMessage());
                    }
                }
            }
        };
    }

}
