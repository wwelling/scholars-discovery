package edu.tamu.scholars.discovery.etl.transform.solr;

import static edu.tamu.scholars.discovery.AppConstants.CLASS;
import static edu.tamu.scholars.discovery.AppConstants.ID;
import static edu.tamu.scholars.discovery.AppConstants.LABEL;
import static edu.tamu.scholars.discovery.AppConstants.NESTED_DELIMITER;
import static edu.tamu.scholars.discovery.AppConstants.SYNC_IDS;
import static edu.tamu.scholars.discovery.etl.EtlConstants.NESTED_DELIMITER_PATTERN;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.common.SolrInputDocument;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.transform.DataTransformer;
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

        String id = data.get(ID).toString();

        document.setField(ID, id);
        document.setField(CLASS, data.get(CLASS));

        document.setField(SYNC_IDS, id);

        for (DataField field : this.data.getFields()) {
            processField(new RootContext(data, document, id), field.getDescriptor());
        }

        return document;
    }

    private void processField(RootContext rootContext, DataFieldDescriptor descriptor) {
        if (descriptor.isNested()) {
            processNestedField(rootContext, descriptor);
        } else {
            processSimpleField(rootContext, descriptor);
        }
    }

    private void processNestedField(RootContext rootContext, DataFieldDescriptor descriptor) {
        Object object = rootContext.data.get(descriptor.getName());

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
                .map(parts -> processNestedValue(rootContext, rootContext.id, descriptor, parts, 1))
                .toList();

            if (!documents.isEmpty()) {
                rootContext.document.setField(name, documents);
            }
        } else {
            String[] parts = NESTED_DELIMITER_PATTERN.split(object.toString());
            if (parts.length > 1) {
                rootContext.document.setField(name, processNestedValue(rootContext, rootContext.id, descriptor, parts, 1));
            }
        }
    }

    private void processSimpleField(RootContext rootContext, DataFieldDescriptor descriptor) {
        Object object = rootContext.data.get(descriptor.getName());

        if (object == null) {
            return;
        }

        String type = descriptor.getDestination().getType();

        if (descriptor.getDestination().isMultiValued()) {
            @SuppressWarnings("unchecked")
            List<String> values = ((List<String>) object).stream()
                .map(value -> processValue(type, value))
                .toList();

            rootContext.document.setField(descriptor.getName(), values);
        } else {
            rootContext.document.setField(descriptor.getName(), processValue(type, object.toString()));
        }
    }

    private SolrInputDocument processNestedValue(RootContext rootContext, String rootDocumentId, DataFieldDescriptor descriptor, String[] parts, int index) {
        SolrInputDocument nestedDocument = new SolrInputDocument();

        String id = parts[index];

        String nestedDocumentId = rootDocumentId + NESTED_DELIMITER + parts[index];
        String nestedDocumentLabel = parts[0];

        nestedDocument.setField(ID, nestedDocumentId);
        nestedDocument.setField(LABEL, nestedDocumentLabel);

        rootContext.document.addField(SYNC_IDS, id);

        processNestedReferences(rootContext, id, descriptor, nestedDocument, parts, index + 1);

        return nestedDocument;
    }

    public void processNestedReferences(RootContext rootContext, String rootDocumentId, DataFieldDescriptor descriptor, SolrInputDocument nestedDocument, String[] parts, int depth) {
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {
            processNestedReference(rootContext, rootDocumentId, nestedDescriptor, nestedDocument, parts, depth);
        }
    }

    public void processNestedReference(RootContext rootContext, String rootDocumentId, DataFieldDescriptor nestedDescriptor, SolrInputDocument nestedDocument, String[] parts, int depth) {
        boolean isMultiValued = nestedDescriptor.getDestination().isMultiValued();

        if (isMultiValued) {
            processMultiValuedNestedReference(rootContext, rootDocumentId, nestedDescriptor, nestedDocument, parts, depth);
        } else {
            processSingleValuedNestedReference(rootContext, rootDocumentId, nestedDescriptor, nestedDocument, depth);
        }
    }

    private void processMultiValuedNestedReference(RootContext rootContext, String rootDocumentId, DataFieldDescriptor nestedDescriptor, SolrInputDocument nestedDocument, String[] parts, int depth) {
        Object nestedObject = rootContext.data.get(nestedDescriptor.getName());

        if (nestedObject == null) {
            return;
        }

        @SuppressWarnings("unchecked")
        List<String> nestedValues = (List<String>) nestedObject;

        if (nestedValues.isEmpty()) {
            return;
        }

        if (hasNestedStructure(nestedValues, depth)) {
            processNestedStructure(rootContext, rootDocumentId, nestedDescriptor, nestedDocument, nestedValues, parts, depth);
        } else {
            String type = nestedDescriptor.getDestination().getType();

            processSimpleStructure(nestedDescriptor, nestedDocument, nestedValues, parts, type);
        }
    }

    private void processSingleValuedNestedReference(RootContext rootContext, String rootDocumentId, DataFieldDescriptor nestedDescriptor, SolrInputDocument nestedDocument, int depth) {
        Object nestedObject = rootContext.data.get(nestedDescriptor.getName());

        if (nestedObject == null) {
            return;
        }

        String name = getFieldName(nestedDescriptor);

        String[] nestedParts = NESTED_DELIMITER_PATTERN.split(nestedObject.toString());

        if (nestedParts.length > depth) {
            nestedDocument.setField(name, processNestedValue(rootContext, rootDocumentId, nestedDescriptor, nestedParts, depth));
        } else {
            if (nestedParts[0] != null) {
                String type = nestedDescriptor.getDestination().getType();

                nestedDocument.setField(name, processValue(type, nestedParts[0]));
            }
        }
    }

    private void processNestedStructure(RootContext rootContext, String rootDocumentId, DataFieldDescriptor nestedDescriptor, SolrInputDocument nestedDocument, List<String> nestedValues, String[] parts, int depth) {
        List<SolrInputDocument> documents = nestedValues.stream()
            .filter(nv -> isProperty(parts, nv))
            .map(NESTED_DELIMITER_PATTERN::split)
            .map(nvParts -> processNestedValue(rootContext, rootDocumentId, nestedDescriptor, nvParts, depth))
            .toList();

        if (documents.isEmpty()) {
            return;
        }

        String name = getFieldName(nestedDescriptor);

        if (isMultipleReference(nestedDescriptor)) {
            nestedDocument.setField(name, documents);
        } else {
            nestedDocument.setField(name, documents.get(0));
        }
    }

    private void processSimpleStructure(DataFieldDescriptor nestedDescriptor, SolrInputDocument nestedDocument, List<String> nestedValues, String[] parts, String type) {
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
            nestedDocument.setField(name, collection);
        } else {
            nestedDocument.setField(name, collection.get(0));
        }
    }

    private String getFieldName(DataFieldDescriptor descriptor) {
        return descriptor.getNestedReference() != null
            && isNotEmpty(descriptor.getNestedReference().getKey())
                ? descriptor.getNestedReference().getKey()
                : descriptor.getName();
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

    private boolean hasNestedStructure(List<String> values, int depth) {
        return NESTED_DELIMITER_PATTERN.split(values.get(0)).length > depth;
    }

    private boolean isProperty(String[] parts, String value) {
        for (int i = parts.length - 1; i > 0; i--) {
            if (!value.contains(parts[i])) {
                return false;
            }
        }

        return true;
    }

    private boolean isMultipleReference(DataFieldDescriptor descriptor) {
        return descriptor.getNestedReference() != null
            && descriptor.getNestedReference().getMultiple() != null
            && descriptor.getNestedReference().getMultiple();
    }

    private record RootContext(Map<String, Object> data, SolrInputDocument document, String id) {

    }

}
