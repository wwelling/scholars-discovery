package edu.tamu.scholars.discovery.index.component.jena;

import static edu.tamu.scholars.discovery.index.IndexConstants.CLASS;
import static edu.tamu.scholars.discovery.index.IndexConstants.ID;
import static edu.tamu.scholars.discovery.index.IndexConstants.NESTED_DELIMITER;
import static edu.tamu.scholars.discovery.index.IndexConstants.SYNC_IDS;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.index.annotation.CollectionSource;
import edu.tamu.scholars.discovery.index.annotation.FieldSource;
import edu.tamu.scholars.discovery.index.annotation.FieldType;
import edu.tamu.scholars.discovery.index.annotation.NestedObject;
import edu.tamu.scholars.discovery.index.annotation.FieldSource.CacheableLookup;
import edu.tamu.scholars.discovery.index.component.Harvester;
import edu.tamu.scholars.discovery.index.model.AbstractIndexDocument;
import edu.tamu.scholars.discovery.service.CacheService;
import edu.tamu.scholars.discovery.service.TemplateService;
import edu.tamu.scholars.discovery.service.Triplestore;

public class TriplestoreHarvester implements Harvester {

    private static final Logger logger = LoggerFactory.getLogger(TriplestoreHarvester.class);

    private static final String COLLECTION = "collection";

    private static final String FORWARD_SLASH = "/";

    private static final String HASH_TAG = "#";

    // private static final String NESTED = "nested";

    @Autowired
    private Triplestore triplestore;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private CacheService cacheService;

    private final Class<AbstractIndexDocument> type;

    private final List<Field> fields;

    // private final List<Field> nestedFields;

    public TriplestoreHarvester(Class<AbstractIndexDocument> type) {
        this.type = type;
        this.fields = FieldUtils.getFieldsListWithAnnotation(type, FieldSource.class);
        // this.nestedFields = FieldUtils.getFieldsListWithAnnotation(type, FieldType.class)
        //     .stream()
        //     .filter(this::isNestedField)
        //     .toList();
    }

    public Flux<Map<String, Object>> harvest() {
        CollectionSource source = type.getAnnotation(CollectionSource.class);
        String query = templateService.templateSparql(COLLECTION, source.predicate());
        logger.debug("{}:\n{}", COLLECTION, query);
        QueryExecution queryExecution = triplestore.createQueryExecution(query);
        Iterator<Triple> tripleIterator = queryExecution.execConstructTriples();

        return Flux.fromIterable(() -> tripleIterator)
            .map(this::subject)
            .map(this::harvest)
            .doFinally(onFinally -> queryExecution.close());
    }

    public Map<String, Object> harvest(String subject) {
        try {
            return createIndividual(subject);
        } catch (Exception e) {
            logger.debug("Error harvesting", e);
            logger.error("Unable to harvest {} {}: {}", type.getSimpleName(), parse(subject), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Class<AbstractIndexDocument> type() {
        return type;
    }

    private Map<String, Object> createIndividual(String subject)
        throws IllegalArgumentException, SecurityException {
        Map<String, Object> document = new HashMap<>();

        document.put(ID, parse(subject));

        lookupProperties(document, subject);
        // lookupSyncIds(document);

        document.put(CLASS, type.getSimpleName());

        return document;
    }

    private void lookupProperties(Map<String, Object> document, String subject) {
        fields.parallelStream().forEach(field -> {
            FieldSource source = field.getAnnotation(FieldSource.class);
            try {
                Model model = queryForModel(source, subject);
                List<String> values = lookupProperty(source, model);

                // get cacheable lookup
                if (source.lookup().length > 0) {
                    Set<String> uniqueValues = new HashSet<>();
                    boolean isNestedObject = field.isAnnotationPresent(NestedObject.class);
                    for (CacheableLookup lookup : source.lookup()) {
                        FieldSource cacheableSource = getCacheableSource(source, lookup);
                        for (String value : values) {
                            if (isNestedObject) {
                                String[] parts = value.split(NESTED_DELIMITER);
                                String[] ids = Arrays.copyOfRange(parts, 1, parts.length);
                                String[] refIds = ids.length > 1 ? Arrays.copyOfRange(ids, 0, ids.length - 1) : new String[] {};
                                String newSubjectId = ids[ids.length - 1];
                                String newSubject = subject.replaceAll("[^/]+$", newSubjectId);
                                for (String cached : queryForValues(cacheableSource, newSubject)) {
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
                            } else {
                                for (String cached : queryForValues(cacheableSource, value)) {
                                    uniqueValues.add(cached);
                                }
                            }
                        }
                    }
                    values = new ArrayList<>(uniqueValues);
                }

                if (!values.isEmpty()) {
                    populate(document, field, values.stream().map(v -> v.split(NESTED_DELIMITER)[0]).toList());
                } else {
                    logger.debug("Could not find values for {}", field.getName());
                }
            } catch (Exception e) {
                logger.error(
                    "Unable to populate individual {} {}: {}\n{}",
                    type.getSimpleName(),
                    parse(subject),
                    e.getMessage(),
                    source.template()
                );
                logger.debug("Error populating individual", e);
            }
        });
    }

    private List<String> queryForValues(FieldSource source, String subject) {
        String key = toKey(source, subject);
        List<String> values = cacheService.get(key);
        if (Objects.isNull(values)) {
            Model model = queryForModel(source, subject);
            values = lookupProperty(source, model);
            cacheService.put(key, values);
        }

        return values;
    }

    private String toKey(FieldSource source, String subject) {
        return String.format("%s::%s::%s", source.predicate(), source.template(), subject);
    }

    private Model queryForModel(FieldSource source, String subject) {
        String query = templateService.templateSparql(source.template(), subject);
        logger.debug("{}:\n{}", source.template(), query);
        try (QueryExecution qe = triplestore.createQueryExecution(query)) {
            Model model = qe.execConstruct();
            if (logger.isDebugEnabled()) {
                model.write(System.out, "RDF/XML");
            }

            return model;
        }
    }

    private List<String> lookupProperty(FieldSource source, Model model) {
        List<String> values = new ArrayList<>();
        ResIterator resources = model.listSubjects();

        Property property = model.createProperty(source.predicate());

        while (resources.hasNext()) {
            Resource resource = resources.next();
            values.addAll(queryForProperty(source, resource, property));
        }

        return values;
    }

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

            if (source.parse()) {
                value = parse(value);
            }

            value = value.replace("\\\"", "\"");

            if (value.contains("^^")) {
                value = value.substring(0, value.indexOf("^^"));
            }

            if (source.unique() && values.stream().anyMatch(value::equalsIgnoreCase)) {
                logger.debug("duplicate value {}", value);
            } else {
                if (source.split()) {
                    values.addAll(Arrays.asList(value.split("\\|\\|")));
                } else {
                    values.add(value);
                }
            }
        }

        return values;
    }

    private void populate(
        Map<String, Object> document,
        Field field,
        List<String> values
    ) throws IllegalArgumentException {
        if (values.isEmpty()) {
            logger.debug("Could not find values for {}", field.getName());
        } else {
            if (Collection.class.isAssignableFrom(field.getType())) {
                document.put(field.getName(), values);
            } else {
                document.put(field.getName(), values.get(0));
            }
        }
    }

    // private void lookupSyncIds(Map<String, Object> document) {
    //     Set<String> syncIds = new HashSet<>();

    //     nestedFields.forEach(field -> {
    //         Object value = document.get(field.getName());
    //         if (Objects.nonNull(value)) {
    //             if (Collection.class.isAssignableFrom(field.getType())) {
    //                 @SuppressWarnings("unchecked")
    //                 List<String> values = (List<String>) value;
    //                 values.forEach(v -> addSyncId(syncIds, v));
    //             } else {
    //                 addSyncId(syncIds, (String) value);
    //             }
    //         }
    //     });

    //     document.put(SYNC_IDS, new ArrayList<>(syncIds));
    // }

    // private void addSyncId(Set<String> syncIds, String value) {
    //     String[] parts = value.split(NESTED_DELIMITER);
    //     Collections.addAll(syncIds, Arrays.copyOfRange(parts, 1, parts.length));
    // }

    // private boolean isNestedField(Field field) {
    //     return field.getAnnotation(FieldType.class)
    //         .type()
    //         .startsWith(NESTED);
    // }

    private String subject(Triple triple) {
        return triple.getSubject()
            .toString();
    }

    private String parse(String uri) {
        return uri.substring(uri.lastIndexOf(uri.contains(HASH_TAG) ? HASH_TAG : FORWARD_SLASH) + 1);
    }

    private FieldSource getCacheableSource(FieldSource source, CacheableLookup lookup) {
        return new FieldSource() {
            @Override
            public String template() {
                return lookup.template();
            }

            @Override
            public String predicate() {
                return lookup.predicate();
            }

            @Override
            public boolean parse() {
                return source.parse();
            }

            @Override
            public boolean unique() {
                return source.unique();
            }

            @Override
            public boolean split() {
                return source.split();
            }

            @Override
            public CacheableLookup[] lookup() {
                return new CacheableLookup[] {};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                throw new UnsupportedOperationException("Unimplemented method 'annotationType'");
            }
        };
    }

}
