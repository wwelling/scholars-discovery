package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.repo.DataFieldDescriptorRepo;
import edu.tamu.scholars.discovery.etl.model.repo.DataRepo;

@Service
public class DataDefaults extends AbstractDefaults<Data, DataRepo> {

    private final DataFieldDescriptorRepo descriptorRepo;

    public DataDefaults(
            MiddlewareConfig config,
            ResourcePatternResolver resolver,
            DataRepo repo,
            DataFieldDescriptorRepo descriptorRepo) {
        super(config, resolver, repo);
        this.descriptorRepo = descriptorRepo;
    }

    @Override
    public String path() {
        return "classpath:defaults/data/*.{yml,yaml}";
    }

    @Override
    public List<Data> read(Resource[] resources) throws IOException {
        return loadResources(resources, Data.class);
    }

    @Override
    public List<Class<?>> getDefaultDependencies() {
        return List.of(ExtractorDefaults.class, TransformerDefaults.class, LoaderDefaults.class);
    }

    @Override
    protected void save(Data source) {
        updateFields(source, source);
        repo.save(source);
    }

    @Override
    protected void copyProperties(Data source, Data target) {
        target.setName(source.getName());
        target.setCollectionSource(source.getCollectionSource());
        target.setExtractor(source.getExtractor());
        target.setTransformer(source.getTransformer());
        target.setLoader(source.getLoader());

        updateFields(source, target);
    }

    private void updateFields(Data source, Data target) {
        Set<DataField> sourceFields = Optional.ofNullable(source.getFields()).orElse(Set.of());
        Set<DataField> targetFields = Optional.ofNullable(target.getFields()).orElse(Set.of());

        Map<String, DataField> existingFieldsMap = targetFields.stream()
            .collect(Collectors.toMap(field -> field.getDescriptor().getName(), Function.identity()));

        Set<DataField> updatedFields = sourceFields.stream()
            .map(sourceField -> {
                DataField targetField = existingFieldsMap.getOrDefault(sourceField.getDescriptor().getName(), new DataField());

                targetField.setData(target);
                targetField.setDescriptor(resolveDescriptor(sourceField.getDescriptor()));
                targetField.setNestedDescriptors(resolveNestedDescriptors(sourceField.getNestedDescriptors()));

                return targetField;
            }).collect(Collectors.toSet());

        target.setFields(updatedFields);
    }

    private DataFieldDescriptor resolveDescriptor(DataFieldDescriptor descriptor) {
        return descriptorRepo.findByName(descriptor.getName())
            .orElseGet(() -> descriptorRepo.save(descriptor));
    }

    private Set<DataFieldDescriptor> resolveNestedDescriptors(Set<DataFieldDescriptor> descriptors) {
        return Optional.ofNullable(descriptors)
            .orElse(Set.of())
            .stream()
            .map(this::resolveDescriptor)
            .collect(Collectors.toSet());
    }
}