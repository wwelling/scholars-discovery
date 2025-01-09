package edu.tamu.scholars.discovery.etl.transform.solr;

import static edu.tamu.scholars.discovery.AppConstants.CLASS;
import static edu.tamu.scholars.discovery.AppConstants.COLLECTIONS;
import static edu.tamu.scholars.discovery.AppConstants.ID;
import static edu.tamu.scholars.discovery.AppConstants.ID_PATH_DELIMITER;
import static edu.tamu.scholars.discovery.AppConstants.SYNC_IDS;
import static edu.tamu.scholars.discovery.AppConstants.TYPE_S;
import static edu.tamu.scholars.discovery.etl.EtlConstants.NESTED_DELIMITER_PATTERN;
import static edu.tamu.scholars.discovery.etl.EtlUtility.getFieldPrefix;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        Set<String> syncIds = new HashSet<>();
        SolrInputDocument document = new SolrInputDocument();

        String id = data.get(ID).toString();

        document.setField(ID, id);

        document.setField(CLASS, data.get(CLASS));

        document.setField(SYNC_IDS, syncIds);

        for (DataField field : this.data.getFields()) {
            processField(new RootContext(data, syncIds, document, id), field.getDescriptor());
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

        String key = StringUtils.isNotEmpty(descriptor.getNestPath())
            ? descriptor.getNestPath()
            : descriptor.getName();

        if (descriptor.getDestination().isMultiValued()) {

            rootContext.document.addField(COLLECTIONS, key);

            List<String> values = (List<String>) object;

            List<SolrInputDocument> documents = values.stream()
                .map(NESTED_DELIMITER_PATTERN::split)
                .filter(parts -> parts.length > 1)
                .map(parts -> processNestedValue(
                        rootContext,
                        rootContext.id,
                        descriptor,
                        parts,
                        1
                    )
                ).toList();

            if (!documents.isEmpty()) {
                rootContext.document.setField(key, documents);
            }
        } else {
            String[] parts = NESTED_DELIMITER_PATTERN.split(object.toString());
            if (parts.length > 1) {
                rootContext.document.setField(
                    key,
                    processNestedValue(
                        rootContext,
                        rootContext.id,
                        descriptor,
                        parts,
                        1
                    )
                );
            }
        }
    }

    private void processSimpleField(RootContext rootContext, DataFieldDescriptor descriptor) {
        String name = descriptor.getName();
        Object object = rootContext.data.get(name);

        if (object == null) {
            return;
        }

        String type = descriptor.getDestination().getType();

        if (descriptor.getDestination().isMultiValued()) {

            rootContext.document.addField(COLLECTIONS, name);

            List<String> values = ((List<String>) object).stream()
                .map(value -> processValue(type, value))
                .toList();

            rootContext.document.setField(name, values);
        } else {
            rootContext.document.setField(name, processValue(type, object.toString()));
        }
    }

    private SolrInputDocument processNestedValue(
        RootContext rootContext,
        String rootDocumentId,
        DataFieldDescriptor descriptor,
        String[] parts,
        int index
    ) {
        SolrInputDocument nestedDocument = new SolrInputDocument();

        String name = descriptor.getName();
        String type = descriptor.getDestination().getType();

        String id = parts[index];

        String nestedDocumentId = rootDocumentId + ID_PATH_DELIMITER + parts[index];

        String nestedDocumentValue = processValue(type, parts[0]);

        nestedDocument.setField(ID, nestedDocumentId);

        nestedDocument.setField(name, nestedDocumentValue);

        nestedDocument.setField(TYPE_S, getFieldPrefix(descriptor));

        rootContext.syncIds.add(id);

        processNestedReferences(
            rootContext,
            nestedDocumentId,
            descriptor,
            nestedDocument,
            parts,
            index + 1
        );

        return nestedDocument;
    }

    public void processNestedReferences(
        RootContext rootContext,
        String rootDocumentId,
        DataFieldDescriptor descriptor,
        SolrInputDocument nestedDocument,
        String[] parts,
        int depth
    ) {
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {
            processNestedReference(
                rootContext,
                rootDocumentId,
                nestedDescriptor,
                nestedDocument,
                parts,
                depth
            );
        }
    }

    public void processNestedReference(
        RootContext rootContext,
        String rootDocumentId,
        DataFieldDescriptor nestedDescriptor,
        SolrInputDocument nestedDocument,
        String[] parts,
        int depth
    ) {
        boolean isMultiValued = nestedDescriptor.getDestination().isMultiValued();

        if (isMultiValued) {
            processMultiValuedNestedReference(
                rootContext,
                rootDocumentId,
                nestedDescriptor,
                nestedDocument,
                parts,
                depth
            );
        } else {
            processSingleValuedNestedReference(
                rootContext,
                rootDocumentId,
                nestedDescriptor,
                nestedDocument,
                depth
            );
        }
    }

    private void processMultiValuedNestedReference(
        RootContext rootContext,
        String rootDocumentId,
        DataFieldDescriptor nestedDescriptor,
        SolrInputDocument nestedDocument,
        String[] parts,
        int depth
    ) {
        Object nestedObject = rootContext.data.get(nestedDescriptor.getName());

        if (nestedObject == null) {
            return;
        }

        List<String> nestedValues = (List<String>) nestedObject;

        if (nestedValues.isEmpty()) {
            return;
        }

        if (hasNestedStructure(nestedValues, depth)) {
            processNestedStructure(
                rootContext,
                rootDocumentId,
                nestedDescriptor,
                nestedDocument,
                nestedValues,
                parts,
                depth
            );
        } else {
            String type = nestedDescriptor.getDestination().getType();

            processSimpleStructure(
                nestedDescriptor,
                nestedDocument,
                nestedValues,
                parts,
                type
            );
        }
    }

    private void processSingleValuedNestedReference(
        RootContext rootContext,
        String rootDocumentId,
        DataFieldDescriptor nestedDescriptor,
        SolrInputDocument nestedDocument,
        int depth
    ) {
        Object nestedObject = rootContext.data.get(nestedDescriptor.getName());

        if (nestedObject == null) {
            return;
        }

        String[] nestedParts = NESTED_DELIMITER_PATTERN.split(nestedObject.toString());

        if (nestedParts.length > depth) {
            String key = StringUtils.isNotEmpty(nestedDescriptor.getNestPath())
                ? nestedDescriptor.getNestPath()
                : nestedDescriptor.getName();

            nestedDocument.setField(
                key,
                processNestedValue(
                    rootContext,
                    rootDocumentId,
                    nestedDescriptor,
                    nestedParts,
                    depth
                )
            );
        } else {
            if (nestedParts[0] != null) {
                String name = nestedDescriptor.getName();

                String type = nestedDescriptor.getDestination().getType();

                nestedDocument.setField(name, processValue(type, nestedParts[0]));
            }
        }
    }

    private void processNestedStructure(
        RootContext rootContext,
        String rootDocumentId,
        DataFieldDescriptor nestedDescriptor,
        SolrInputDocument nestedDocument,
        List<String> nestedValues,
        String[] parts,
        int depth
    ) {
        List<SolrInputDocument> documents = nestedValues.stream()
            .filter(nv -> isProperty(parts, nv))
            .map(NESTED_DELIMITER_PATTERN::split)
            .map(nvParts -> processNestedValue(
                    rootContext,
                    rootDocumentId,
                    nestedDescriptor,
                    nvParts,
                    depth
                )
            ).toList();

        if (documents.isEmpty()) {
            return;
        }

        String key = StringUtils.isNotEmpty(nestedDescriptor.getNestPath())
            ? nestedDescriptor.getNestPath()
            : nestedDescriptor.getName();

        if (nestedDescriptor.isMultiple()) {
            nestedDocument.setField(key, documents);
        } else {
            nestedDocument.setField(key, documents.get(0));
        }
    }

    private void processSimpleStructure(
        DataFieldDescriptor nestedDescriptor,
        SolrInputDocument nestedDocument,
        List<String> nestedValues,
        String[] parts,
        String type
    ) {
        List<String> collection = nestedValues.stream()
            .filter(nv -> isProperty(parts, nv))
            .map(nv -> NESTED_DELIMITER_PATTERN.split(nv)[0])
            .map(value -> processValue(type, value))
            .toList();

        if (collection.isEmpty()) {
            return;
        }

        String name = nestedDescriptor.getName();

        if (nestedDescriptor.isMultiple()) {
            nestedDocument.setField(name, collection);
        } else {
            nestedDocument.setField(name, collection.get(0));
        }
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

    private record RootContext(
        Map<String, Object> data,
        Set<String> syncIds,
        SolrInputDocument document,
        String id
    ) { }

}
