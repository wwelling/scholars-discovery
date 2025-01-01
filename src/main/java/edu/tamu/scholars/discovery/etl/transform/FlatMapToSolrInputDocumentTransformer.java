package edu.tamu.scholars.discovery.etl.transform;

import static edu.tamu.scholars.discovery.index.IndexConstants.CLASS;
import static edu.tamu.scholars.discovery.index.IndexConstants.ID;
import static edu.tamu.scholars.discovery.index.IndexConstants.LABEL;
import static edu.tamu.scholars.discovery.index.IndexConstants.NESTED_DELIMITER;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.NestedReference;
import edu.tamu.scholars.discovery.utility.DateFormatUtility;

@Slf4j
public class FlatMapToSolrInputDocumentTransformer implements DataTransformer<Map<String, Object>, SolrInputDocument> {

    private final Data data;

    public FlatMapToSolrInputDocumentTransformer(Data data) {
        this.data = data;
    }

    @Override
    public SolrInputDocument transform(Map<String, Object> data) {
        SolrInputDocument document = new SolrInputDocument();

        document.setField(ID, data.remove(ID));
        document.setField(CLASS, data.remove(CLASS));

        processFields(data, document);

        // System.out.println("\n" + this.data.getName() + "\n" + document + "\n");

        return document;
    }

    private void processFields(Map<String, Object> data, SolrInputDocument document) {
        this.data.getFields()
                .stream()
                .map(DataField::getDescriptor)
                .forEach(descriptor -> processField(data, descriptor, document));
    }

    // TODO: cleanup

    // TODO: reduce Cognitive Complexity from 20 to the 15 allowed
    private void processField(Map<String, Object> data, DataFieldDescriptor descriptor, SolrInputDocument document) {
        String name = descriptor.getName();
        Object object = data.remove(name);
        if (Objects.nonNull(object)) {
            if (descriptor.isNested()) {
                if (descriptor.getDestination().isMultiValued()) {
                    @SuppressWarnings("unchecked")
                    List<String> values = (List<String>) object;

                    List<SolrInputDocument> documents = values.stream()
                            .map(value -> value.split(NESTED_DELIMITER))
                            .filter(parts -> parts.length > 1)
                            .map(parts -> processNestedValue(data, descriptor, parts, 1))
                            .toList();

                    if (!documents.isEmpty()) {

                        for (SolrInputDocument doc : documents) {
                            if (doc == null) {
                                System.out.println("\n\n\nNULL HERE+\n\n\n");
                            }
                        }

                        document.addChildDocuments(documents);
                    }
                } else {
                    String[] parts = object.toString().split(NESTED_DELIMITER);
                    if (parts.length > 1) {

                        SolrInputDocument doc = processNestedValue(data, descriptor, parts, 1);
                        if (doc == null) {
                            System.out.println("\n\n\n1 NULL HERE\n\n\n");
                        }

                        document.addChildDocument(doc);
                    }
                }
            } else {
                if (descriptor.getDestination().isMultiValued()) {
                    @SuppressWarnings("unchecked")
                    List<String> values = (List<String>) object;

                    if (descriptor.getDestination().getType().startsWith("pdate")) {
                        List<String> formattedDates = new ArrayList<>();
                        for (String value : values) {
                            try {
                                formattedDates.add(DateFormatUtility.parse(value));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        values = formattedDates;
                    }

                    document.setField(descriptor.getName(), values);
                } else {
                    if (descriptor.getDestination().getType().startsWith("pdate")) {
                        try {
                            document.setField(descriptor.getName(), DateFormatUtility.parse(object.toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        document.setField(descriptor.getName(), object.toString());
                    }
                }
            }
        }
    }

    private SolrInputDocument processNestedValue(Map<String, Object> data, DataFieldDescriptor descriptor, String[] parts, int index) {
        SolrInputDocument childDocument = new SolrInputDocument();

        childDocument.setField("_nest_path_", "/" + getFieldName(descriptor));

        childDocument.setField(ID, parts[index]);
        childDocument.setField(LABEL, parts[0]);

        processNestedReferences(data, descriptor, childDocument, parts, index + 1);

        return childDocument;
    }

    // TODO: reduce Cognitive Complexity from 57 to the 15 allowed
    public void processNestedReferences(Map<String, Object> data, DataFieldDescriptor descriptor, SolrInputDocument childDocument, String[] parts, int depth) {
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {
            Object nestedObject = data.remove(nestedDescriptor.getName());
            if (Objects.nonNull(nestedObject)) {
                if (nestedDescriptor.getDestination().isMultiValued()) {
                    @SuppressWarnings("unchecked")
                    List<String> nestedValues = (List<String>) nestedObject;

                    if (!nestedValues.isEmpty()) {
                        if (nestedValues.get(0).split(NESTED_DELIMITER).length > depth) {

                            List<SolrInputDocument> documents = nestedValues.stream()
                                    .filter(nv -> isProperty(parts, nv))
                                    .map(nv -> nv.split(NESTED_DELIMITER))
                                    .filter(nvParts -> nvParts.length > 1)
                                    .map(nvParts -> processNestedValue(data, nestedDescriptor, nvParts, depth))
                                    .toList();

                            for (SolrInputDocument doc : documents) {
                                if (doc == null) {
                                    System.out.println("\n\n\nNULL HERE++\n\n\n");
                                }
                            }

                            if (!documents.isEmpty()) {
                                boolean isMultiple = Objects.nonNull(nestedDescriptor.getNestedReference())
                                        && Objects.nonNull(nestedDescriptor.getNestedReference().getMultiple())
                                        && nestedDescriptor.getNestedReference().getMultiple();

                                if (isMultiple) {
                                    childDocument.addChildDocuments(documents);
                                } else {
                                    childDocument.addChildDocument(documents.get(0));
                                }
                            }
                        } else {
                            List<String> collection = nestedValues.stream()
                                    .filter(nv -> isProperty(parts, nv))
                                    .map(nv -> nv.split(NESTED_DELIMITER)[0])
                                    .toList();

                            if (!collection.isEmpty()) {
                                boolean isMultiple = Objects.nonNull(nestedDescriptor.getNestedReference())
                                        && Objects.nonNull(nestedDescriptor.getNestedReference().getMultiple())
                                        && nestedDescriptor.getNestedReference().getMultiple();

                                if (nestedDescriptor.getDestination().getType().startsWith("pdate")) {
                                    List<String> formattedDates = new ArrayList<>();
                                    for (String value : collection) {
                                        try {
                                            formattedDates.add(DateFormatUtility.parse(value));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    collection = formattedDates;
                                }

                                if (isMultiple) {
                                    childDocument.setField(nestedDescriptor.getNestedReference().getKey(), collection);
                                } else {
                                    childDocument.setField(nestedDescriptor.getNestedReference().getKey(), collection.get(0));
                                }
                            }
                        }
                    }
                } else {
                    String[] nestedParts = nestedObject.toString().split(NESTED_DELIMITER);

                    if (nestedParts.length > depth) {
                        SolrInputDocument doc = processNestedValue(data, nestedDescriptor, nestedParts, depth);
                        if (doc == null) {
                            System.out.println("\n\n\n2 NULL HERE\n\n\n");
                        }

                        childDocument.addChildDocument(doc);
                    } else {
                        if (nestedParts[0] != null) {
                            if (nestedDescriptor.getDestination().getType().startsWith("pdate")) {
                                try {
                                    childDocument.setField(nestedDescriptor.getNestedReference().getKey(), DateFormatUtility.parse(nestedParts[0]));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                childDocument.setField(nestedDescriptor.getNestedReference().getKey(), nestedParts[0]);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isProperty(String[] parts, String value) {
        for (int i = parts.length - 1; i > 0; i--) {
            if (!value.contains(parts[i])) {
                return false;
            }
        }

        return true;
    }

    private String getFieldName(DataFieldDescriptor descriptor) {
        return Objects.nonNull(descriptor.getNestedReference())
                && StringUtils.isNotEmpty(descriptor.getNestedReference().getKey())
                        ? descriptor.getNestedReference().getKey()
                        : descriptor.getName();
    }

}
