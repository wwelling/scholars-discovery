package edu.tamu.scholars.discovery.utility;

import static edu.tamu.scholars.discovery.index.DiscoveryConstants.CLASS;
import static edu.tamu.scholars.discovery.index.DiscoveryConstants.DISCOVERY_MODEL_PACKAGE;
import static edu.tamu.scholars.discovery.index.DiscoveryConstants.PATH_DELIMETER_REGEX;
import static edu.tamu.scholars.discovery.index.DiscoveryConstants.REQUEST_PARAM_DELIMETER;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import edu.tamu.scholars.discovery.index.annotation.CollectionSource;
import edu.tamu.scholars.discovery.index.annotation.FieldType;
import edu.tamu.scholars.discovery.index.annotation.NestedObject;
import edu.tamu.scholars.discovery.index.annotation.NestedObject.Reference;

public class DiscoveryUtility {

    private static final Map<String, Class<?>> TYPES = new HashMap<>();

    private static final Map<String, Map<String, Class<?>>> TYPE_FIELDS = new HashMap<>();

    private static final BidiMap<String, String> MAPPING = new DualHashBidiMap<>();

    private static final List<String> CREATED_FIELDS = new CopyOnWriteArrayList<>();

    static {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(CollectionSource.class));
        Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents(DISCOVERY_MODEL_PACKAGE);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                Class<?> type = Class.forName(beanDefinition.getBeanClassName());
                TYPES.put(type.getSimpleName(), type);

                Map<String, Class<?>> fields = FieldUtils.getFieldsListWithAnnotation(type, FieldType.class)
                        .stream()
                        .collect(Collectors.toMap(field -> {
                            FieldType fieldType = field.getAnnotation(FieldType.class);
                            return StringUtils.isNotEmpty(fieldType.name())
                                    ? fieldType.name()
                                    : field.getName();
                        }, Field::getType));

                fields.put(CLASS, String.class);
                TYPE_FIELDS.put(type.getSimpleName(), fields);

            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to find class for " + beanDefinition.getBeanClassName(), e);
            }
        }
    }

    private DiscoveryUtility() {

    }

    /**
     * Adds a field to created fields if not already exists. Returns false
     * otherwise.
     * 
     * @param name then name of the field
     * @return whether the field existed already
     */
    public static boolean addField(String name) {
        if (CREATED_FIELDS.contains(name)) {
            return true;
        }
        CREATED_FIELDS.add(name);

        return false;
    }

    public static Class<?> getDiscoveryDocumentType(String name) {
        Optional<Class<?>> documentType = Optional.ofNullable(TYPES.get(name));
        if (documentType.isPresent()) {
            return documentType.get();
        }
        throw new RuntimeException("Unable to find class for " + name);
    }

    public static Map<String, Class<?>> getDiscoveryDocumentTypeFields(String name) {
        Optional<Map<String, Class<?>>> documentTypeFields = Optional.ofNullable(TYPE_FIELDS.get(name));
        if (documentTypeFields.isPresent()) {
            return documentTypeFields.get();
        }
        throw new RuntimeException("Unable to find class for " + name);
    }

    public static String[] processFields(String[] fields) {
        return processFields(Arrays.asList(fields))
                .toArray(new String[fields.length]);
    }

    public static List<String> processFields(List<String> fields) {
        return fields
                .stream()
                .map(DiscoveryUtility::findProperty)
                .toList();
    }

    public static String processFields(String fields) {
        if (StringUtils.isNoneEmpty(fields)) {
            String parameter = StringUtils.EMPTY;
            for (String field : fields.split(REQUEST_PARAM_DELIMETER)) {
                parameter += findProperty(field) + REQUEST_PARAM_DELIMETER;
            }
            return StringUtils.removeEnd(parameter, REQUEST_PARAM_DELIMETER);
        }
        return StringUtils.EMPTY;
    }

    public static String findPath(String property) {
        String actualProperty = MAPPING.getKey(property);
        if (StringUtils.isNoneEmpty(actualProperty)) {
            return actualProperty;
        }
        return property;
    }

    public static String findProperty(String path) {
        String actualPath = MAPPING.get(path);
        if (StringUtils.isNotEmpty(actualPath)) {
            return actualPath;
        }
        List<String> properties = new ArrayList<>(Arrays.asList(path.split(PATH_DELIMETER_REGEX)));
        for (Class<?> type : TYPES.values()) {
            Optional<String> property = findProperty(type, properties);
            if (property.isPresent()) {
                MAPPING.put(path, property.get());
                return property.get();
            }
        }
        MAPPING.put(path, path);
        return path;
    }

    private static Optional<String> findProperty(Class<?> type, List<String> properties) {
        String property = properties.get(0);
        Field field = FieldUtils.getField(type, property, true);
        if (field != null) {
            if (properties.size() == 1) {
                return Optional.of(field.getName());
            }
            NestedObject nestedObject = field.getAnnotation(NestedObject.class);
            if (nestedObject != null) {
                String referenceProperty = properties.get(1);
                for (Reference reference : nestedObject.properties()) {
                    if (reference.key().equals(referenceProperty)) {
                        properties.remove(0);
                        properties.set(0, reference.value());
                        return findProperty(type, properties);
                    }
                }
            }
        }
        return Optional.empty();
    }

}
