package edu.tamu.scholars.discovery.etl.extract;

import static edu.tamu.scholars.discovery.index.IndexConstants.CLASS;
import static edu.tamu.scholars.discovery.index.IndexConstants.ID;
import static edu.tamu.scholars.discovery.index.IndexConstants.NESTED_DELIMITER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.etl.model.CacheableSource;
import edu.tamu.scholars.discovery.etl.model.CollectionSource;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.FieldSource;

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
            Query query = getQuery(template, predicate);

            log.debug("{}", query);

            QueryExecution queryExecution = createQueryExecution(query.toString());
            Iterator<Triple> tripleIterator = queryExecution.execConstructTriples();

            return Flux.fromIterable(() -> tripleIterator)
                .map(this::subject)
                .map(this::harvest)
                .doFinally(onFinally -> queryExecution.close());

        } catch (Exception e) { // TODO: determine exact exceptions thrown and handle individually
            log.error("Unable to extract {}: {}", data.getName(), e.getMessage());
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

    private Map<String, Object> harvest(String subject) {
        return createIndividual(subject);
    }

    private Map<String, Object> createIndividual(String subject) {
        Map<String, Object> document = new HashMap<>();

        document.put(ID, parse(subject));
        document.put(CLASS, data.getName());

        lookupProperties(document, subject);

        return document;
    }

    private void lookupProperties(Map<String, Object> document, String subject) {
        fields.parallelStream().forEach(field -> {
            DataFieldDescriptor descriptor = field.getDescriptor();

            try {
                List<String> values = getValues(descriptor, subject);

                if (!values.isEmpty()) {
                    populate(document, descriptor, values);
                } else {
                    log.debug("Could not find values for {}", descriptor.getName());
                }
            } catch (Exception e) { // TODO: determine exact exceptions thrown and handle individually
                log.error("Unable to extracting individual {} {} ({}): {}",
                    data.getName(),
                    descriptor.getName(),
                    parse(subject),
                    e.getMessage());
                log.debug("Error extracting individual", e);
            }
        });
    }

    private List<String> getValues(DataFieldDescriptor descriptor, String subject) {
        FieldSource source = descriptor.getSource();

        List<String> values = getValues(source, subject);

        Set<CacheableSource> cacheableSources = source.getCacheableSources();

        if (!cacheableSources.isEmpty()) {
            return new ArrayList<>(getCacheableValues(descriptor, values, subject));
        }

        return values;
    }

    private List<String> getValues(FieldSource source, String subject) {
        Query query = getQuery(source.getTemplate(), subject);

        return queryValues(source, query);
    }

    private Query getQuery(String template, String subject) {
        ParameterizedSparqlString parameterizedSparql = new ParameterizedSparqlString();
        parameterizedSparql.setCommandText(template);
        parameterizedSparql.setIri("uri", subject);

        Query query = QueryFactory.create(parameterizedSparql.toString());

        log.debug("{}", query);

        return query;
    }

    private List<String> queryValues(FieldSource source, Query query) {
        List<String> values = new ArrayList<>();

        Model model = queryModel(query);

        ResIterator resources = model.listSubjects();

        Property property = model.createProperty(source.getPredicate());

        while (resources.hasNext()) {
            Resource resource = resources.next();
            values.addAll(getValues(source, resource, property));
        }

        model.close();
        resources.close();

        return values;
    }

    private Model queryModel(Query query) {
        log.debug("\n{}", query.toString());
        try (QueryExecution qe = createQueryExecution(query.toString())) {
            Model model = qe.execConstruct();
            if (log.isDebugEnabled()) {
                model.write(System.out, "RDF/XML");
            }

            return model;
        }
    }

    private Set<String> getCacheableValues(DataFieldDescriptor descriptor, List<String> values, String subject) {
        FieldSource source = descriptor.getSource();
        Set<CacheableSource> cacheableSources = source.getCacheableSources();

        Set<String> uniqueValues = new HashSet<>();
        for (CacheableSource cacheableSource : cacheableSources) {

            FieldSource cacheableFieldSource = getCacheableSource(source, cacheableSource);

            for (String value : values) {
                if (descriptor.isNested()) {
                    uniqueValues.addAll(getNestedCacheableValues(cacheableFieldSource, value, subject));
                } else {
                    for (String cached : getCacheOrQueryValues(cacheableFieldSource, value)) {
                        uniqueValues.add(cached);
                    }
                }
            }
        }

        return uniqueValues;
    }

    private Set<String> getNestedCacheableValues(FieldSource cacheableFieldSource, String value, String subject) {
        Set<String> uniqueValues = new HashSet<>();

        String[] parts = value.split(NESTED_DELIMITER);
        String[] ids = Arrays.copyOfRange(parts, 1, parts.length);
        String[] refIds = ids.length > 1 ? Arrays.copyOfRange(ids, 0, ids.length - 1) : new String[] {};
        String newSubjectId = ids[ids.length - 1];
        String newSubject = subject.replaceAll("[^/]+$", newSubjectId);
        for (String cached : getCacheOrQueryValues(cacheableFieldSource, newSubject)) {
            String[] cachedParts = cached.split(NESTED_DELIMITER, 2);
            String label = cachedParts[0];
            StringBuilder result = new StringBuilder(label);
            if (refIds.length > 0) {
                result.append(NESTED_DELIMITER);
                result.append(String.join(NESTED_DELIMITER, refIds));
            }
            if (cachedParts.length > 1) {
                result.append(NESTED_DELIMITER);
                result.append(cachedParts[1]);
            }

            uniqueValues.add(result.toString());
        }

        return uniqueValues;
    }

    private List<String> getCacheOrQueryValues(FieldSource source, String subject) {
        Query query = getQuery(source.getTemplate(), subject);

        String cacheKey = query.toString();

        List<String> values = ExtractorCacheUtility.get(cacheKey);

        if (Objects.isNull(values)) {
            values = queryValues(source, query);
            ExtractorCacheUtility.put(cacheKey, values);
        }

        return values;
    }

    private void populate(Map<String, Object> document, DataFieldDescriptor descriptor, List<String> values) {
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

        statements.close();

        return values;
    }

    private String parse(String uri) {
        return uri.substring(uri.lastIndexOf(uri.contains(HASH_TAG) ? HASH_TAG : FORWARD_SLASH) + 1);
    }

    private FieldSource getCacheableSource(FieldSource source, CacheableSource cacheableSource) {
        return new FieldSource() {
            @Override
            public String getTemplate() {
                return cacheableSource.getTemplate();
            }

            @Override
            public String getPredicate() {
                return cacheableSource.getPredicate();
            }

            @Override
            public boolean isParse() {
                return source.isParse();
            }

            @Override
            public boolean isUnique() {
                return source.isUnique();
            }

            @Override
            public boolean isSplit() {
                return source.isSplit();
            }

            @Override
            public Set<CacheableSource> getCacheableSources() {
                return Set.of();
            }
        };
    }

    protected abstract QueryExecution createQueryExecution(String query);

}
