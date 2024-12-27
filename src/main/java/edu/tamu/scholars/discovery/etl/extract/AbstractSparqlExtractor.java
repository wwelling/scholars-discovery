package edu.tamu.scholars.discovery.etl.extract;

import static edu.tamu.scholars.discovery.index.IndexConstants.CLASS;
import static edu.tamu.scholars.discovery.index.IndexConstants.ID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.etl.model.CollectionSource;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.FieldSource;
import edu.tamu.scholars.discovery.utility.TemplateUtility;

@Slf4j
public abstract class AbstractSparqlExtractor implements DataExtractor<Map<String, Object>> {

    private static final String FORWARD_SLASH = "/";

    private static final String HASH_TAG = "#";

    protected final Data data;

    protected final Map<String, String> properties;

    protected final CollectionSource collectionSource;

    protected final Set<DataField> fields;

    protected AbstractSparqlExtractor(Data data) {
        this.data = data;
        this.properties = data.getExtractor().getAttributes();
        this.collectionSource = data.getCollectionSource();
        this.fields = data.getFields();
    }

    @Override
    public Flux<Map<String, Object>> extract() {
        String template = collectionSource.getTemplate();
        String predicate = collectionSource.getPredicate();

        try {
            String query = TemplateUtility.templateSparql(template, predicate);

            log.info("{}", query);

            QueryExecution queryExecution = createQueryExecution(query);
            Iterator<Triple> tripleIterator = queryExecution.execConstructTriples();

            return Flux.fromIterable(() -> tripleIterator)
                    .map(this::subject)
                    .map(this::harvest)
                    .doFinally(onFinally -> queryExecution.close());

        } catch (IOException e) {
            log.error("Unable to extract {}: {}",
                    data.getName(),
                    e.getMessage());
            log.debug("Error extracting individual", e);
        }

        return Flux.empty();
    }

    @Override
    public Map<String, Object> extract(String subject) {
        return Map.of();
    }

    private String subject(Triple triple) {
        return triple.getSubject()
                .toString();
    }

    public Map<String, Object> harvest(String subject) {
        log.info("{}", subject);
        try {
            return createIndividual(subject);
        } catch (Exception e) {
            log.debug("Error harvesting", e);
            log.error("Unable to harvest {}: {}", parse(subject), e.getMessage());
        }

        return Map.of();
    }

    private Map<String, Object> createIndividual(String subject)
            throws IllegalArgumentException, SecurityException {
        Map<String, Object> document = new HashMap<>();

        document.put(ID, parse(subject));

        lookupProperties(document, subject);

        document.put(CLASS, data.getName());

        return document;
    }

    private void lookupProperties(Map<String, Object> document, String subject) {
        fields.parallelStream()
                .forEach(field -> {
                    DataFieldDescriptor descriptor = field.getDescriptor();
                    FieldSource source = descriptor.getSource();

                    try {
                        Model model = queryForModel(source, subject);

                        List<String> values = lookupProperty(source, model);

                        if (!values.isEmpty()) {
                            populate(document, descriptor, values);
                        } else {
                            log.debug("Could not find values for {}", field.getDescriptor().getName());
                        }

                    } catch (IOException e) {
                        log.error(
                                "Unable to populate individual {} {} ({}): {}",
                                data.getName(),
                                descriptor.getName(),
                                parse(subject),
                                e.getMessage());
                        log.debug("Error populating individual", e);
                    }

                });
    }

    private Model queryForModel(FieldSource source, String subject) throws IOException {
        String query = TemplateUtility.templateSparql(source.getTemplate(), subject);
        log.debug("{}:\n{}", source.getTemplate(), query);

        try (QueryExecution qe = createQueryExecution(query)) {
            Model model = qe.execConstruct();
            if (log.isDebugEnabled()) {
                model.write(System.out, "RDF/XML");
            }

            return model;
        }
    }

    private List<String> lookupProperty(FieldSource source, Model model) {
        List<String> values = new ArrayList<>();
        ResIterator resources = model.listSubjects();

        Property property = model.createProperty(source.getPredicate());

        while (resources.hasNext()) {
            Resource resource = resources.next();
            values.addAll(queryForProperty(source, resource, property));
        }

        return values;
    }

    private void populate(
            Map<String, Object> document,
            DataFieldDescriptor descriptor,
            List<String> values) throws IllegalArgumentException {
        if (values.isEmpty()) {
            log.debug("Could not find values for {}", descriptor.getName());
        } else {
            if (descriptor.getDestination().isMultiValued()) {
                document.put(descriptor.getName(), values);
            } else {
                document.put(descriptor.getName(), values.get(0));
            }
        }
    }

    @SuppressWarnings("java:S3776")
    private List<String> queryForProperty(FieldSource source, Resource resource, Property property) {
        List<String> values = new ArrayList<>();
        StmtIterator statements = resource.listProperties(property);

        while (statements.hasNext()) {
            Statement statement = statements.next();
            RDFNode object = statement.getObject();
            String value = null;

            if (object.isLiteral()) {
                value = object.asLiteral().getValue().toString();
            } else if (object.isResource()) {
                value = object.asResource().getURI();
            } else {
                value = object.toString();
            }

            if (source.isParse()) {
                value = parse(value);
            }

            value = value.replace("\\\"", "\"");

            if (value.contains("^^")) {
                value = value.substring(0, value.indexOf("^^"));
            }

            if (source.isUnique() && values.stream().anyMatch(value::equalsIgnoreCase)) {
                log.debug("duplicate value {}", value);
            } else {
                if (source.isSplit()) {
                    values.addAll(Arrays.asList(value.split("\\|\\|")));
                } else {
                    values.add(value);
                }
            }
        }

        return values;
    }

    private String parse(String uri) {
        return uri.substring(uri.lastIndexOf(uri.contains(HASH_TAG) ? HASH_TAG : FORWARD_SLASH) + 1);
    }

    protected abstract QueryExecution createQueryExecution(String query);

}
