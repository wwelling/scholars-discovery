package edu.tamu.scholars.discovery;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import edu.tamu.scholars.discovery.index.annotation.CollectionSource;
import edu.tamu.scholars.discovery.index.annotation.FieldSource;
import edu.tamu.scholars.discovery.index.annotation.FieldSource.CacheableLookup;
import edu.tamu.scholars.discovery.index.annotation.FieldType;
import edu.tamu.scholars.discovery.index.annotation.NestedMultiValuedProperty;
import edu.tamu.scholars.discovery.index.annotation.NestedObject;
import edu.tamu.scholars.discovery.index.annotation.NestedObject.Reference;
import edu.tamu.scholars.discovery.index.model.Collection;
import edu.tamu.scholars.discovery.index.model.Concept;
import edu.tamu.scholars.discovery.index.model.Document;
import edu.tamu.scholars.discovery.index.model.Organization;
import edu.tamu.scholars.discovery.index.model.Person;
import edu.tamu.scholars.discovery.index.model.Process;
import edu.tamu.scholars.discovery.index.model.Relationship;

@EnableScheduling
@SpringBootApplication
public class MiddlewareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiddlewareApplication.class, args);
    }

    @PostConstruct
    public void initializeTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @PostConstruct
    public void echo() {
        // echo(Collection.class);
        // echo(Concept.class);
        // echo(Document.class);
        // echo(Organization.class);
        // echo(Person.class);
        // echo(Process.class);
        // echo(Relationship.class);
    }

    public void echo(Class<?> clazz) {
        CollectionSource collectionSource = clazz.getAnnotation(CollectionSource.class);

        System.out.println("\n\n");
        System.out.println("name: " + clazz.getSimpleName());
        System.out.println("collectionSource:");
        System.out.println("  template: defaults/data/common/collection.sparql");
        System.out.println("  predicate: " + collectionSource.predicate());
        System.out.println("extractor:");
        System.out.println("  id: 1");
        System.out.println("transformer:");
        System.out.println("  id: 1");
        System.out.println("loader:");
        System.out.println("  id: 1");

        System.out.println("fields:");

        Class<?> cc = clazz;

        while (cc != null) {

            Field[] fields = cc.getDeclaredFields();

            Map<String, Field> references = new HashMap<>();

            for (Field field : fields) {
                field.setAccessible(true);

                FieldType fieldType = field.getAnnotation(FieldType.class);
                if (fieldType == null) {
                    continue;
                }

                FieldSource fieldSource = field.getAnnotation(FieldSource.class);
                if (fieldSource == null) {
                    continue;
                }

                NestedObject nestedObject = field.getAnnotation(NestedObject.class);

                if (nestedObject != null) {
                    Reference[] properties = nestedObject.properties();

                    for (Reference property : properties) {
                        Field nestedField = FieldUtils.getField(cc, property.value(), true);
                        references.put(property.value(), nestedField);
                    }
                }
            }
 
            for (Field field : fields) {
                field.setAccessible(true);

                if (references.containsKey(field.getName())) {
                    continue;
                }

                processField(cc, field, 2, 1);
            }

            cc = cc.getSuperclass();
        }

        System.out.println("\n\n");
    }

    private void processField(Class<?> cc, Field field, int spaces, int depth) {

        FieldType fieldType = field.getAnnotation(FieldType.class);
        if (fieldType == null) {
            return;
        }

        FieldSource fieldSource = field.getAnnotation(FieldSource.class);
        if (fieldSource == null) {
            return;
        }

        String fieldName = field.getName();

        NestedObject nestedObject = field.getAnnotation(NestedObject.class);
        boolean nested = nestedObject != null;

        String name = StringUtils.isNotEmpty(fieldType.name())
            ? fieldType.name()
            : fieldName;

        boolean stored = fieldType.stored();
        boolean indexed = fieldType.indexed();
        boolean docValues = fieldType.docValues();
        String type = fieldType.type();
        String[] copyTo = fieldType.copyTo();
        String defaultValue = fieldType.defaultValue();
        boolean required = fieldType.required();
        boolean multiValued = java.util.Collection.class.isAssignableFrom(field.getType());

        String template = fieldSource.template();
        String predicate = fieldSource.predicate();
        boolean parse = fieldSource.parse();
        boolean unique = fieldSource.unique();
        boolean split = fieldSource.split();

        String indent = " ".repeat(spaces);

        if (depth == 1) System.out.println(indent + "- descriptor:");

        System.out.println(indent + "    name: " + name);

        if (nested || depth > 1) System.out.println(indent + "    nested: " + nested);

        System.out.println(indent + "    destination:");
        System.out.println(indent + "      type: " + type);

        if (StringUtils.isNotEmpty(defaultValue)) {
            System.out.println(indent + "      defaultValue: " + defaultValue);
        }

        if (copyTo.length > 0) {
            System.out.println(indent + "      copyTo:");
            for (String ct : copyTo) {
                System.out.println(indent + "        - " + ct);
            }
        }

        if (required) System.out.println(indent + "      required: " + required);
        if (!stored) System.out.println(indent + "      stored: " + stored);
        if (!indexed) System.out.println(indent + "      indexed: " + indexed);
        if (multiValued) System.out.println(indent + "      multiValued: " + multiValued);
        if (docValues) System.out.println(indent + "      docValues: " + docValues);

        System.out.println(indent + "    source:");
        System.out.println(indent + "      template: defaults/data/" + template + ".sparql");
        System.out.println(indent + "      predicate: " + predicate);
        if (unique) System.out.println(indent + "      unique: " + unique);
        if (parse) System.out.println(indent + "      parse: " + parse);
        if (split) System.out.println(indent + "      split: " + split);

        CacheableLookup[] lookups = fieldSource.lookup();
        if (lookups.length > 0) {
            System.out.println(indent + "      cacheableSources:");
            for (CacheableLookup lookup : lookups) {
                System.out.println(indent + "        - template: defaults/data/" + lookup.template() + ".sparql");
                System.out.println(indent + "          predicate: " + lookup.predicate());
            }
        }

        if (nested) {
            Reference[] properties = nestedObject.properties();

            if (properties.length > 0) {
                System.out.println(indent + "    nestedDescriptors:");
                for (Reference property : properties) {
                    System.out.println(indent + "      - nestedReference:");
                    System.out.println(indent + "          key: " + property.key());

                    Field nestedField = FieldUtils.getField(cc, property.value(), true);

                    NestedMultiValuedProperty nestedMultiValuedProperty = nestedField.getAnnotation(NestedMultiValuedProperty.class);
                    boolean nestedMultivalue = nestedMultiValuedProperty != null;

                    if (nestedMultivalue) System.out.println(indent + "          multiple: " + nestedMultivalue);

                    processField(cc, nestedField, spaces + 4, depth + 1);

                }
            }
        }
    }

}
