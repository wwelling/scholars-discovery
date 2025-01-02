package edu.tamu.scholars.discovery.export.service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletContext;
import jakarta.xml.bind.JAXBException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import edu.tamu.scholars.discovery.export.exception.ExportException;
import edu.tamu.scholars.discovery.model.Individual;
import edu.tamu.scholars.discovery.model.repo.IndividualRepo;
import edu.tamu.scholars.discovery.service.TemplateService;
import edu.tamu.scholars.discovery.view.model.DisplayView;
import edu.tamu.scholars.discovery.view.model.ExportView;
import edu.tamu.scholars.discovery.view.model.repo.DisplayViewRepo;

@Service
public class DocxExporter extends AbstractDocxExporter {

    private static final String TYPE = "docx";

    private static final String CONTENT_TYPE =
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    private static final String CONTENT_DISPOSITION_TEMPLATE = "attachment; filename=%s.docx";

    DocxExporter(
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
        final List<String> types = individual.getTypes();

        Optional<DisplayView> displayView = displayViewRepo.findByTypesIn(types);

        if (!displayView.isPresent()) {
            throw new ExportException(String.format(
                "Could not find a display view for types: %s", String.join(", ", types)));
        }

        Optional<ExportView> exportView = displayView.get()
            .getExportViews()
            .stream()
            .filter(ev -> ev.getName().equalsIgnoreCase(name))
            .findAny();

        if (!exportView.isPresent()) {
            throw new ExportException(String.format(
                "%s display view does not have an export view named %s",
                displayView.get().getName(),
                name
            ));
        }

        return outputStream -> {
            try {
                final ObjectNode node = mapper.valueToTree(individual);
                final WordprocessingMLPackage pkg = createDocx(node, exportView.get());

                pkg.save(outputStream);

            } catch (JAXBException | Docx4JException e) {
                throw new ExportException(e.getMessage());
            }
        };
    }

}
