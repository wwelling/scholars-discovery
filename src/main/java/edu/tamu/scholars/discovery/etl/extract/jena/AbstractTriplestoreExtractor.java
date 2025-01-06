package edu.tamu.scholars.discovery.etl.extract.jena;

import static edu.tamu.scholars.discovery.AppConstants.CLASS;
import static edu.tamu.scholars.discovery.AppConstants.ID;
import static edu.tamu.scholars.discovery.etl.extract.jena.TriplestoreCacheUtility.computePropertyIfAbsent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.model.CollectionSource;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.FieldSource;

@Slf4j
public abstract class AbstractTriplestoreExtractor implements DataExtractor<Map<String, Object>> {

    private static final String FORWARD_SLASH = "/";
    private static final String HASH_TAG = "#";
    private static final String URI_TEMPLATE_KEY = "{{uri}}";

    protected final Data data;

    protected final Map<String, String> properties;

    protected final CollectionSource collectionSource;

    protected AbstractTriplestoreExtractor(Data data) {
        this.data = data;
        this.properties = data.getExtractor().getAttributes();
        this.collectionSource = data.getCollectionSource();
    }

    @Override
    public Flux<Map<String, Object>> extract() {
        String template = collectionSource.getTemplate();
        String predicate = collectionSource.getPredicate();

        try {
            String query = buildQuery(template, predicate);

            Iterator<Triple> tripleIterator = queryCollection(query);

            return Flux.fromIterable(() -> tripleIterator)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(Triple::getSubject)
                .map(Node::toString)
                .map(this::harvest)
                .sequential();

        } catch (Exception e) {
            log.error("Unable to extract {}: {}", data.getName(), e.getMessage());
            log.debug("Error extracting individuals", e);
        }

        return Flux.empty();
    }

    @Override
    public Map<String, Object> extract(String subject) {
        return harvest(subject);
    }

    private Map<String, Object> harvest(String subject) {
        Map<String, Object> document = new HashMap<>();

        document.put(ID, parse(subject));
        document.put(CLASS, data.getName());

        extractProperties(document, subject);

        return document;
    }

    private void extractProperties(Map<String, Object> document, String subject) {
        for (DataField field : data.getFields()) {
            extractProperties(document, subject, field.getDescriptor());
        }
    }

    private void extractProperties(Map<String, Object> document, String subject, DataFieldDescriptor descriptor) {
        extractProperty(document, subject, descriptor);
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {
            extractProperties(document, subject, nestedDescriptor);
        }
    }

    private void extractProperty(Map<String, Object> document, String subject, DataFieldDescriptor descriptor) {
        try {
            List<String> values = extractValues(descriptor, subject);

            if (values.isEmpty()) {
                log.debug("Could not find values for {}", descriptor.getName());
            } else {
                if (descriptor.getDestination().isMultiValued()) {
                    document.put(descriptor.getName(), values);
                } else {
                    document.put(descriptor.getName(), values.get(0));
                }
            }
        } catch (Exception e) {
            log.error("Unable to extracting individual {} {} ({}): {}",
                data.getName(),
                descriptor.getName(),
                parse(subject),
                e.getMessage());
            log.debug("Error extracting individual", e);
        }
    }

    private List<String> extractValues(DataFieldDescriptor descriptor, String subject) {
        FieldSource source = descriptor.getSource();

        String query = buildQuery(source.getTemplate(), subject);

        return querySourceValues(source, query);
    }

    private List<String> querySourceValues(FieldSource source, String query) {
        List<String> values = new ArrayList<>();

        Model model = null;
        ResIterator resources = null;

        try {
            model = queryIndividual(query);
            resources = model.listSubjects();
            Property property = getCachePropertyOrCreate(model, source.getPredicate());

            while (resources.hasNext()) {
                Resource resource = resources.next();
                values.addAll(getValues(source, resource, property));
            }

            return values;
        } finally {
            if (resources != null) {
                resources.close();
            }
            if (model != null) {
                model.close();
            }
        }
    }

    private Property getCachePropertyOrCreate(Model model, String predicate) {
        return computePropertyIfAbsent(predicate, model::createProperty);
    }

    private List<String> getValues(FieldSource source, Resource resource, Property property) {
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

            value = sanatizeValue(value);

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

        statements.close();

        return values;
    }

    private String sanatizeValue(String value) {
        value = value.replace("\\\"", "\"");

        if (value.contains("^^")) {
            value = value.substring(0, value.indexOf("^^"));
        }

        return value;
    }

    private String buildQuery(String template, String subject) {
        String query = template.replace(URI_TEMPLATE_KEY, subject);

        log.debug("{}", query);

        return query;
    }

    private String parse(String uri) {
        return uri.substring(uri.lastIndexOf(uri.contains(HASH_TAG) ? HASH_TAG : FORWARD_SLASH) + 1);
    }

    protected abstract Iterator<Triple> queryCollection(String query);

    protected abstract Model queryIndividual(String query);

}
