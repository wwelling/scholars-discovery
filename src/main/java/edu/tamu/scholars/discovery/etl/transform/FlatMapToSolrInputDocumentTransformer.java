package edu.tamu.scholars.discovery.etl.transform;

import static edu.tamu.scholars.discovery.index.IndexConstants.CLASS;
import static edu.tamu.scholars.discovery.index.IndexConstants.ID;
import static edu.tamu.scholars.discovery.index.IndexConstants.LABEL;
import static edu.tamu.scholars.discovery.index.IndexConstants.NESTED_DELIMITER;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
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

        String id = data.remove(ID).toString();

        document.setField(ID, id);
        document.setField(CLASS, data.remove(CLASS));

        processFields(data, id, document);

        return document;
    }

    private void processFields(Map<String, Object> data, String id, SolrInputDocument document) {
        for (DataField field : this.data.getFields()) {
            processField(data, id, field.getDescriptor(), document);
        }
    }

    // TODO: reduce Cognitive Complexity from 20 to the 15 allowed
    private void processField(Map<String, Object> data, String id, DataFieldDescriptor descriptor, SolrInputDocument document) {
        String name = descriptor.getName();

        Object object = data.remove(name);

        if (object != null) {
            if (descriptor.isNested()) {
                if (descriptor.getDestination().isMultiValued()) {
                    @SuppressWarnings("unchecked")
                    List<String> values = (List<String>) object;

                    List<SolrInputDocument> documents = values.stream()
                        .map(value -> value.split(NESTED_DELIMITER))
                        .filter(parts -> parts.length > 1)
                        .map(parts -> processNestedValue(data, id, descriptor, parts, 1))
                        .toList();

                    if (!documents.isEmpty()) {
                        document.addChildDocuments(documents);
                    }
                } else {
                    String[] parts = object.toString().split(NESTED_DELIMITER);
                    if (parts.length > 1) {
                        document.addChildDocument(processNestedValue(data, id, descriptor, parts, 1));
                    }
                }
            } else {
                String type = descriptor.getDestination().getType();

                if (descriptor.getDestination().isMultiValued()) {
                    @SuppressWarnings("unchecked")
                    List<String> values = ((List<String>) object).stream()
                        .map(value -> process(type, value))
                        .toList();

                    document.setField(descriptor.getName(), values);
                } else {
                    document.setField(descriptor.getName(), process(type, object.toString()));
                }
            }
        }
    }

    private SolrInputDocument processNestedValue(Map<String, Object> data, String parentId, DataFieldDescriptor descriptor, String[] parts, int index) {
        SolrInputDocument childDocument = new SolrInputDocument();

        childDocument.setField("_nest_path_", "/" + getFieldName(descriptor));

        String id = parentId + "!" + parts[index];

        childDocument.setField(ID, id);
        childDocument.setField(LABEL, parts[0]);

        processNestedReferences(data, id, descriptor, childDocument, parts, index + 1);

        return childDocument;
    }

    // TODO: reduce Cognitive Complexity from 56 to the 15 allowed
    public void processNestedReferences(Map<String, Object> data, String parentId, DataFieldDescriptor descriptor, SolrInputDocument childDocument, String[] parts, int depth) {
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {
            Object nestedObject = data.remove(nestedDescriptor.getName());

            if (nestedObject != null) {
                String type = nestedDescriptor.getDestination().getType();

                if (nestedDescriptor.getDestination().isMultiValued()) {
                    @SuppressWarnings("unchecked")
                    List<String> nestedValues = (List<String>) nestedObject;

                    if (!nestedValues.isEmpty()) {
                        boolean isMultiple = nestedDescriptor.getNestedReference() != null
                            && nestedDescriptor.getNestedReference().getMultiple() != null
                            && nestedDescriptor.getNestedReference().getMultiple();

                        if (nestedValues.get(0).split(NESTED_DELIMITER).length > depth) {

                            List<SolrInputDocument> documents = nestedValues.stream()
                                .filter(nv -> isProperty(parts, nv))
                                .map(nv -> nv.split(NESTED_DELIMITER))
                                .map(nvParts -> processNestedValue(data, parentId, nestedDescriptor, nvParts, depth))
                                .toList();

                            if (!documents.isEmpty()) {
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
                                    .map(value -> process(type, value))
                                    .toList();

                            if (!collection.isEmpty()) {
                                if (isMultiple) {
                                    childDocument.setField(getFieldName(nestedDescriptor), collection);
                                } else {
                                    childDocument.setField(getFieldName(nestedDescriptor), collection.get(0));
                                }
                            }
                        }
                    }
                } else {
                    String[] nestedParts = nestedObject.toString().split(NESTED_DELIMITER);

                    if (nestedParts.length > depth) {
                        childDocument.addChildDocument(processNestedValue(data, parentId, nestedDescriptor, nestedParts, depth));
                    } else {
                        if (nestedParts[0] != null) {
                            childDocument.setField(getFieldName(nestedDescriptor), process(type, nestedParts[0]));
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

    private String process(String type, String value) {
        if (type.startsWith("pdate")) {
            try {
                return DateFormatUtility.parse(value);
            } catch (ParseException e) {
                log.warn("Failed to parse date {}: {}", value, e.getMessage());
            }
        }

        return value;
    }

    private String getFieldName(DataFieldDescriptor descriptor) {
        return Objects.nonNull(descriptor.getNestedReference())
            && StringUtils.isNotEmpty(descriptor.getNestedReference().getKey())
                ? descriptor.getNestedReference().getKey()
                : descriptor.getName();
    }

}
