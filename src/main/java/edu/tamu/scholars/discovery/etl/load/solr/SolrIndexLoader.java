package edu.tamu.scholars.discovery.etl.load.solr;

import static edu.tamu.scholars.discovery.AppConstants.CLASS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.solr.common.SolrInputDocument;

import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.FieldDestination;
import edu.tamu.scholars.discovery.factory.index.dto.CopyField;
import edu.tamu.scholars.discovery.factory.index.dto.Field;
import edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndex;
import edu.tamu.scholars.discovery.factory.index.solr.ManagedSolrIndexFactory;

@Slf4j
public class SolrIndexLoader implements DataLoader<SolrInputDocument> {

    private final Data data;

    private final ManagedSolrIndex index;

    private final Map<String, Field> existingFields;

    private final Map<Pair<String, String>, CopyField> existingCopyFields;

    public SolrIndexLoader(Data data) {
        this.data = data;

        this.index = ManagedSolrIndexFactory.of(data.getLoader().getAttributes());

        this.existingFields = new HashMap<>();
        this.existingCopyFields = new HashMap<>();
    }

    @Override
    public void init() {
        Map<String, Field> fields = this.index.getFields()
            .stream()
            .collect(Collectors.toMap(Field::getName, Function.identity()));

        this.existingFields.putAll(fields);

        Map<Pair<String, String>, CopyField> copyFields = this.index.getCopyFields()
            .stream()
            .flatMap(field -> field.getDest().stream()
                .map(dest -> Map.entry(Pair.of(field.getSource(), dest), field)))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing));

        this.existingCopyFields.putAll(copyFields);
    }

    @Override
    public void preprocess() {
        preprocessFields();
    }

    @Override
    public void postprocess() {
        this.index.optimize();
    }

    @Override
    public void destroy() {
        this.index.close();
    }

    @Override
    public void load(Collection<SolrInputDocument> documents) {
        this.index.update(documents);
        log.info("{} {} documents loaded.", documents.size(), this.data.getName());
    }

    @Override
    public void load(SolrInputDocument document) {
        this.index.update(document);
    }

    private void preprocessFields() {
        log.info("Preprocessing fields for {} documents.", this.data.getName());

        List<Field> fields = new ArrayList<>();
        List<CopyField> copyFields = new ArrayList<>();

        processField(getDescriptor(CLASS, false, false), fields, copyFields);
        processField(getDescriptor("_collections_", false, true), fields, copyFields);
        processField(getDescriptor("_nest_parent_", false, false), fields, copyFields);

        this.data.getFields()
            .stream()
            .map(DataField::getDescriptor)
            .forEach(descriptor -> processFields(descriptor, fields, copyFields));

        if (fields.isEmpty()) {
            log.debug("{} index field already exist.", this.data.getName());
        } else {
            this.index.addFields(fields);
        }

        if (copyFields.isEmpty()) {
            log.debug("{} index copy fields already exist.", this.data.getName());
        } else {
            this.index.addCopyFields(copyFields);
        }
    }

    private void processFields(DataFieldDescriptor descriptor, List<Field> fields, List<CopyField> copyFields) {
        processField(descriptor, fields, copyFields);
        for (DataFieldDescriptor nestedDescriptor : descriptor.getNestedDescriptors()) {
           processFields(nestedDescriptor, fields, copyFields);
        }
    }

    private void processField(DataFieldDescriptor descriptor, List<Field> fields, List<CopyField> copyFields) {
        String name = descriptor.getName();

        Field field = Field.of(descriptor);

        log.debug("Processing field {}", name);

        if (!existingFields.containsKey(name)) {
            fields.add(field);
            existingFields.put(name, field);
        } else {
            log.debug("field {} already exists", name);
        }

        Set<String> copyTo = descriptor
            .getDestination()
            .getCopyTo();

        if (copyTo.isEmpty()) {
            return;
        }

        for (String dest : copyTo) {
            log.debug("Processing copy field {} => {}", name, dest);
            Pair<String, String> key = Pair.of(name, dest);
            if (!existingCopyFields.containsKey(key)) {
                CopyField copyField = CopyField.of(name, dest);
                copyFields.add(copyField);
                existingCopyFields.put(key, copyField);
            } else {
                log.debug("copy field {} => {} already exists", name, dest);
            }
        }
    }

    private DataFieldDescriptor getDescriptor(String name, boolean docValues, boolean multiValued) {
        DataFieldDescriptor descriptor = new DataFieldDescriptor();
        FieldDestination destination = new FieldDestination();
        destination.setDocValues(docValues);
        destination.setMultiValued(multiValued);

        descriptor.setName(name);
        descriptor.setDestination(destination);

        return descriptor;
    }

}
