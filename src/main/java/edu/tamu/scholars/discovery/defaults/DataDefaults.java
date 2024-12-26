package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    private final DataFieldDescriptorRepo dataFieldDescriptorRepo;

    public DataDefaults(
            MiddlewareConfig config,
            ResourcePatternResolver resolver,
            DataRepo repo,
            DataFieldDescriptorRepo dataFieldDescriptorRepo) {
        super(config, resolver, repo);
        this.dataFieldDescriptorRepo = dataFieldDescriptorRepo;
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
    protected void copyProperties(Data source, Data target) {
        target.setName(source.getName());
        target.setCollectionSource(source.getCollectionSource());

        List<DataField> sourceFields = Optional.ofNullable(source.getFields())
            .orElse(List.of());

        List<DataField> targetFields = Optional.ofNullable(target.getFields())
            .orElse(new ArrayList<>());

        Map<String, DataField> existingFieldsByName = targetFields.stream()
            .collect(Collectors.toMap(DataField::getName, Function.identity()));

        sourceFields.forEach(sourceField -> {
            DataField targetField = existingFieldsByName.remove(sourceField.getName());
            if (targetField != null) {
                copyProperties(sourceField, targetField);
            } else {
                targetFields.add(sourceField);
            }
        });

        targetFields.removeIf(field -> existingFieldsByName.containsKey(field.getName()));
    }

    private void copyProperties(DataField source, DataField target) {
        target.setName(source.getName());

        if (Objects.nonNull(source.getDescriptor())) {
            target.setDescriptor(findOrCreate(source.getDescriptor()));
        }

        List<DataFieldDescriptor> sourceNestedDescriptors = Optional.ofNullable(source.getNestedDescriptors())
            .orElse(List.of());

        List<DataFieldDescriptor> targetNestedDescriptors = sourceNestedDescriptors.stream()
            .map(this::findOrCreate)
            .toList();

        target.setNestedDescriptors(targetNestedDescriptors);
    }

    private DataFieldDescriptor findOrCreate(DataFieldDescriptor sourceDescriptor) {
        return dataFieldDescriptorRepo.findByName(sourceDescriptor.getName())
            .orElseGet(() -> sourceDescriptor);
    }

}
