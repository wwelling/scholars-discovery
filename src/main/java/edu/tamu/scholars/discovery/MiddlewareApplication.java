package edu.tamu.scholars.discovery;

import java.lang.reflect.Field;
import java.util.TimeZone;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import edu.tamu.scholars.discovery.index.annotation.CollectionSource;
import edu.tamu.scholars.discovery.index.annotation.FieldSource;
import edu.tamu.scholars.discovery.index.annotation.FieldSource.CacheableLookup;
import edu.tamu.scholars.discovery.index.annotation.FieldType;
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
        System.out.println("  version: 1");
        System.out.println("transformer:");
        System.out.println("  id: 1");
        System.out.println("  version: 1");
        System.out.println("loader:");
        System.out.println("  id: 1");
        System.out.println("  version: 1");

        System.out.println("fields:");

        Class<?> cc = clazz;

        while (cc != null) {

            Field[] fields = cc.getDeclaredFields();
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

                System.out.println("  - descriptor:");

                System.out.println("      name: " + name);

                if (nested) {
                    System.out.println("      nested: " + nested);
                    if (nestedObject.root()) {
                        System.out.println("      root: " + nestedObject.root());
                    }

                    Reference[] properties = nestedObject.properties();

                    if (properties.length > 0) {
                        System.out.println("      nestedReferences:");
                        for (Reference property : properties) {
                            System.out.println("        - field: " + property.value());
                            System.out.println("          key: " + property.key());
                        }
                    }
                }

                System.out.println("      destination:");
                System.out.println("        type: " + type);

                if (StringUtils.isNotEmpty(defaultValue)) {
                    System.out.println("        defaultValue: " + defaultValue);
                }

                if (copyTo.length > 0) {
                    System.out.println("        copyTo:");
                    for (String ct : copyTo) {
                        System.out.println("          - " + ct);
                    }
                }

                if (required) System.out.println("        required: " + required);
                if (!stored) System.out.println("        stored: " + stored);
                if (!indexed) System.out.println("        indexed: " + indexed);
                if (multiValued) System.out.println("        multiValued: " + multiValued);
                if (docValues) System.out.println("        docValues: " + docValues);

                System.out.println("      source:");
                System.out.println("        template: defaults/data/" + template + ".sparql");
                System.out.println("        predicate: " + predicate);
                if (unique) System.out.println("        unique: " + unique);
                if (parse) System.out.println("        parse: " + parse);
                if (split) System.out.println("        split: " + split);

                CacheableLookup[] lookups = fieldSource.lookup();
                if (lookups.length > 0) {
                    System.out.println("        cacheableSources:");
                    for (CacheableLookup lookup : lookups) {
                        System.out.println("          - template: defaults/data/" + lookup.template() + ".sparql");
                        System.out.println("            predicate: " + lookup.predicate());
                    }
                }
            }

            cc = cc.getSuperclass();
        }

        System.out.println("\n\n");
    }

}
