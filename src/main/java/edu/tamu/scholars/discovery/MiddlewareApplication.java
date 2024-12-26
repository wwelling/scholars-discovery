package edu.tamu.scholars.discovery;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import edu.tamu.scholars.discovery.index.annotation.CollectionSource;
import edu.tamu.scholars.discovery.index.annotation.FieldSource;
import edu.tamu.scholars.discovery.index.annotation.FieldType;
import edu.tamu.scholars.discovery.index.annotation.NestedObject;
import edu.tamu.scholars.discovery.index.annotation.NestedObject.Reference;
import edu.tamu.scholars.discovery.index.model.Collection;
import edu.tamu.scholars.discovery.index.model.Concept;
import edu.tamu.scholars.discovery.index.model.Document;
import edu.tamu.scholars.discovery.index.model.Organization;
import edu.tamu.scholars.discovery.index.model.Person;
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
        // echo(edu.tamu.scholars.discovery.index.model.Process.class);
        // echo(Relationship.class);
    }

    public void echo(Class<?> clazz) {
        CollectionSource collectionSource = clazz.getAnnotation(CollectionSource.class);

        System.out.println("\n\n");
        System.out.println("name: " + clazz.getSimpleName());
        System.out.println("collectionSource:");
        System.out.println("  template: collection.sparql");
        System.out.println("  predicate: " + collectionSource.predicate());
        System.out.println("  extractor:");
        System.out.println("    id: 1");
        System.out.println("    version: 1");

        System.out.println("fields:");

        Map<String, String> nestedProperties = new HashMap<>();

        Class<?> cc = clazz;

        while (cc != null) {

            // System.out.println("\n\n" + cc + "\n\n");

            Field[] fields = cc.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                // System.out.println("\n\n" + field + "\n\n");

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

                boolean readonly = fieldType.readonly();
                boolean stored = fieldType.stored();
                boolean indexed = fieldType.indexed();
                boolean docValues = fieldType.docValues();
                String type = fieldType.type();
                String[] copyTo = fieldType.copyTo();
                String defaultValue = fieldType.defaultValue();
                boolean required = fieldType.required();


                String template = fieldSource.template();
                String predicate = fieldSource.predicate();
                boolean parse = fieldSource.parse();
                boolean unique = fieldSource.unique();
                boolean split = fieldSource.split();

                String nestedProperty = nestedProperties.remove(fieldName);

                if (nestedProperty == null) {

                    System.out.println("  - name: " + name);
                    System.out.println("    descriptor:");

                    System.out.println("      name: " + name);

                    if (nested) {
                        System.out.println("      nested: " + nested);

                        Reference[] properties = nestedObject.properties();

                        if (nestedObject.properties().length > 0) {
                            for (Reference property : properties) {
                                nestedProperties.put(property.value(), property.key());
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
                            System.out.println("        - " + ct);
                        }
                    }

                    System.out.println("        required: " + required);
                    System.out.println("        readonly: " + readonly);
                    System.out.println("        stored: " + stored);
                    System.out.println("        indexed: " + indexed);
                    System.out.println("        docValues: " + docValues);
                    System.out.println("        transformer:");
                    System.out.println("          id: 1");
                    System.out.println("          version: 1");
                    System.out.println("        loader:");
                    System.out.println("          id: 1");
                    System.out.println("          version: 1");

                    System.out.println("      source:");
                    System.out.println("        template: " + template);
                    System.out.println("        predicate: " + predicate);
                    System.out.println("        unique: " + unique);
                    System.out.println("        parse: " + parse);
                    System.out.println("        split: " + split);
                    System.out.println("        extractor:");
                    System.out.println("          id: 1");
                    System.out.println("          version: 1");

                } else {

                    System.out.println("    nestedDescriptors:");

                    System.out.println("      - name: " + name);

                    if (nested) {
                        System.out.println("        nested: " + nested);

                        Reference[] properties = nestedObject.properties();

                        if (nestedObject.properties().length > 0) {
                            for (Reference property : properties) {
                                nestedProperties.put(property.value(), property.key());
                            }
                        }
                    }

                    System.out.println("        destination:");
                    System.out.println("          type: " + type);

                    if (StringUtils.isNotEmpty(defaultValue)) {
                        System.out.println("          defaultValue: " + defaultValue);
                    }

                    if (copyTo.length > 0) {
                        System.out.println("          copyTo:");
                        for (String ct : copyTo) {
                            System.out.println("          - " + ct);
                        }
                    }

                    System.out.println("          required: " + required);
                    System.out.println("          readonly: " + readonly);
                    System.out.println("          stored: " + stored);
                    System.out.println("          indexed: " + indexed);
                    System.out.println("          docValues: " + docValues);
                    System.out.println("          transformer:");
                    System.out.println("            id: 1");
                    System.out.println("            version: 1");
                    System.out.println("          loader:");
                    System.out.println("            id: 1");
                    System.out.println("            version: 1");

                    System.out.println("        source:");
                    System.out.println("          template: " + template);
                    System.out.println("          predicate: " + predicate);
                    System.out.println("          unique: " + unique);
                    System.out.println("          parse: " + parse);
                    System.out.println("          split: " + split);
                    System.out.println("          extractor:");
                    System.out.println("            id: 1");
                    System.out.println("            version: 1");
                }

            }

            cc = cc.getSuperclass();
        }

        System.out.println("\n\n");
    }

}
