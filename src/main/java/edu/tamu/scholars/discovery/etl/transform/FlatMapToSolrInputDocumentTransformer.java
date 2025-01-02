package edu.tamu.scholars.discovery.etl.transform;

import static edu.tamu.scholars.discovery.etl.EtlConstants.NESTED_DELIMITER_PATTERN;
import static edu.tamu.scholars.discovery.index.IndexConstants.CLASS;
import static edu.tamu.scholars.discovery.index.IndexConstants.ID;
import static edu.tamu.scholars.discovery.index.IndexConstants.LABEL;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.common.SolrInputDocument;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.utility.DateFormatUtility;

@Slf4j
public class FlatMapToSolrInputDocumentTransformer implements DataTransformer<Map<String, Object>, SolrInputDocument> {

    private static final String NESTED_ID_DELIMITER = "!";

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

        for (DataField field : this.data.getFields()) {
            processField(data, id, field.getDescriptor(), document);
        }

        return document;
    }

    private void processField(Map<String, Object> data, String id, DataFieldDescriptor descriptor, SolrInputDocument document) {
        if (descriptor.isNested()) {
            processNestedField(data, id, descriptor, document);
        } else {
            processSimpleField(data, descriptor, document);
        }
    }

    private void processNestedField(Map<String, Object> data, String id, DataFieldDescriptor descriptor, SolrInputDocument document) {
        Object object = data.remove(descriptor.getName());

        if (object == null) {
            return;
        }

        String name = getFieldName(descriptor);

        if (descriptor.getDestination().isMultiValued()) {
            @SuppressWarnings("unchecked")
            List<String> values = (List<String>) object;

            List<SolrInputDocument> documents = values.stream()
                .map(NESTED_DELIMITER_PATTERN::split)
                .filter(parts -> parts.length > 1)
                .map(parts -> processNestedValue(data, id, descriptor, parts, 1))
                .toList();

            if (!documents.isEmpty()) {
                document.setField(name, documents);
            }
        } else {
            String[] parts = NESTED_DELIMITER_PATTERN.split(object.toString());
            if (parts.length > 1) {
                document.setField(name, processNestedValue(data, id, descriptor, parts, 1));
            }
        }
    }

    private void processSimpleField(Map<String, Object> data, DataFieldDescriptor descriptor, SolrInputDocument document) {
        Object object = data.remove(descriptor.getName());

        if (object == null) {
            return;
        }

        String type = descriptor.getDestination().getType();

        if (descriptor.getDestination().isMultiValued()) {
            @SuppressWarnings("unchecked")
            List<String> values = ((List<String>) object).stream()
                .map(value -> processValue(type, value))
                .toList();

            document.setField(descriptor.getName(), values);
        } else {
            document.setField(descriptor.getName(), processValue(type, object.toString()));
        }
    }

    private SolrInputDocument processNestedValue(Map<String, Object> data, String parentId, DataFieldDescriptor descriptor, String[] parts, int index) {
        SolrInputDocument childDocument = new SolrInputDocument();

        String id = parentId + NESTED_ID_DELIMITER + parts[index];

        childDocument.setField(ID, id);
        childDocument.setField(LABEL, parts[0]);

        processNestedReferences(data, id, descriptor, childDocument, parts, index + 1);

        return childDocument;
    }

    public void processNestedReferences(Map<String, Object> data, String parentId, DataFieldDescriptor descriptor, SolrInputDocument childDocument, String[] parts, int depth) {
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {
            processNestedReference(data, parentId, nestedDescriptor, childDocument, parts, depth);
        }
    }

    public void processNestedReference(Map<String, Object> data, String parentId, DataFieldDescriptor nestedDescriptor, SolrInputDocument childDocument, String[] parts, int depth) {
        boolean isMultiValued = nestedDescriptor.getDestination().isMultiValued();

        if (isMultiValued) {
            processMultiValuedNestedReference(data, parentId, nestedDescriptor, childDocument, parts, depth);
        } else {
            processSingleValuedNestedReference(data, parentId, nestedDescriptor, childDocument, depth);
        }
    }

    private void processMultiValuedNestedReference(Map<String, Object> data, String parentId, DataFieldDescriptor nestedDescriptor, SolrInputDocument childDocument, String[] parts, int depth) {
        Object nestedObject = data.remove(nestedDescriptor.getName());

        if (nestedObject == null) {
            return;
        }

        @SuppressWarnings("unchecked")
        List<String> nestedValues = (List<String>) nestedObject;

        if (nestedValues.isEmpty()) {
            return;
        }

        if (hasNestedStructure(nestedValues, depth)) {
            processNestedStructure(data, parentId, nestedDescriptor, childDocument, nestedValues, parts, depth);
        } else {
            String type = nestedDescriptor.getDestination().getType();

            processSimpleStructure(nestedDescriptor, childDocument, nestedValues, parts, type);
        }
    }

    private boolean hasNestedStructure(List<String> values, int depth) {
        return NESTED_DELIMITER_PATTERN.split(values.get(0)).length > depth;
    }

    private void processNestedStructure(Map<String, Object> data, String parentId, DataFieldDescriptor nestedDescriptor, SolrInputDocument childDocument, List<String> nestedValues, String[] parts, int depth) {
        List<SolrInputDocument> documents = nestedValues.stream()
            .filter(nv -> isProperty(parts, nv))
            .map(NESTED_DELIMITER_PATTERN::split)
            .map(nvParts -> processNestedValue(data, parentId, nestedDescriptor, nvParts, depth))
            .toList();

        if (documents.isEmpty()) {
            return;
        }

        String name = getFieldName(nestedDescriptor);

        if (isMultipleReference(nestedDescriptor)) {
            childDocument.setField(name, documents);
        } else {
            childDocument.setField(name, documents.get(0));
        }
    }

    private void processSimpleStructure(DataFieldDescriptor nestedDescriptor, SolrInputDocument childDocument, List<String> nestedValues, String[] parts, String type) {
        List<String> collection = nestedValues.stream()
            .filter(nv -> isProperty(parts, nv))
            .map(nv -> NESTED_DELIMITER_PATTERN.split(nv)[0])
            .map(value -> processValue(type, value))
            .toList();

        if (collection.isEmpty()) {
            return;
        }

        String name = getFieldName(nestedDescriptor);

        if (isMultipleReference(nestedDescriptor)) {
            childDocument.setField(name, collection);
        } else {
            childDocument.setField(name, collection.get(0));
        }
    }

    private void processSingleValuedNestedReference(Map<String, Object> data, String parentId, DataFieldDescriptor nestedDescriptor, SolrInputDocument childDocument, int depth) {
        Object nestedObject = data.remove(nestedDescriptor.getName());

        if (nestedObject == null) {
            return;
        }

        String name = getFieldName(nestedDescriptor);

        String[] nestedParts = NESTED_DELIMITER_PATTERN.split(nestedObject.toString());

        if (nestedParts.length > depth) {
            childDocument.setField(name, processNestedValue(data, parentId, nestedDescriptor, nestedParts, depth));
        } else {
            if (nestedParts[0] != null) {
                String type = nestedDescriptor.getDestination().getType();

                childDocument.setField(name, processValue(type, nestedParts[0]));
            }
        }
    }

    private boolean isMultipleReference(DataFieldDescriptor descriptor) {
        return descriptor.getNestedReference() != null
            && descriptor.getNestedReference().getMultiple() != null
            && descriptor.getNestedReference().getMultiple();
    }

    private boolean isProperty(String[] parts, String value) {
        for (int i = parts.length - 1; i > 0; i--) {
            if (!value.contains(parts[i])) {
                return false;
            }
        }

        return true;
    }

    private String processValue(String type, String value) {
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
        return descriptor.getNestedReference() != null
            && isNotEmpty(descriptor.getNestedReference().getKey())
                ? descriptor.getNestedReference().getKey()
                : descriptor.getName();
    }

}
