package edu.tamu.scholars.discovery.defaults;

import static edu.tamu.scholars.discovery.index.IndexConstants.ID;
import static edu.tamu.scholars.discovery.index.IndexConstants.VERSION;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.repo.DataRepo;

@Service
public class DataDefaults extends AbstractDefaults<Data, DataRepo> {

    public DataDefaults(
            MiddlewareConfig config,
            ResourcePatternResolver resolver,
            DataRepo repo) {
        super(config, resolver, repo);
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
    protected void copyProperties(Data source, Data target) {
        target.setName(source.getName());
        target.setCollectionSource(source.getCollectionSource());

        List<DataField> sourceFields = source.getFields();
        List<DataField> targetFields = target.getFields();

        Map<String, DataField> targetFieldsMap = targetFields.stream()
            .collect(Collectors.toMap(DataField::getName, Function.identity()));

        for (DataField sourceField : sourceFields) {
            String name = sourceField.getName();

            DataField targetField = targetFieldsMap.remove(name);

            if (Objects.nonNull(targetField)) {
                // update target data field from source data field
                copyProperties(sourceField, targetField);
            } else {
                // add new data field
                targetFields.add(sourceField);
            }
        }

        // remove data field no longer in source
        targetFields.removeIf(field -> targetFieldsMap.containsKey(field.getName()));
    }

    private void copyProperties(DataField source, DataField target) {
        target.setName(source.getName());

        if (Objects.nonNull(source.getDescriptor())) {
            BeanUtils.copyProperties(source.getDescriptor(), target.getDescriptor(), ID, VERSION);
        }

        List<DataFieldDescriptor> sourceDescriptors = source.getNestedFields();
        List<DataFieldDescriptor> targetDescriptors = target.getNestedFields();

        Map<String, DataFieldDescriptor> targetDescriptorsMap = targetDescriptors.stream()
            .collect(Collectors.toMap(DataFieldDescriptor::getName, Function.identity()));

        for (DataFieldDescriptor sourceDescriptor : sourceDescriptors) {
            String name = sourceDescriptor.getName();

            DataFieldDescriptor targetDescriptor = targetDescriptorsMap.remove(name);

            if (Objects.nonNull(targetDescriptor)) {
                // update target data field descriptor from source data field
                BeanUtils.copyProperties(sourceDescriptor, targetDescriptor, ID, VERSION);
            } else {
                // add new data field descriptor
                targetDescriptors.add(sourceDescriptor);
            }
        }

        // remove data field descriptor no longer in source
        targetDescriptors.removeIf(field -> targetDescriptorsMap.containsKey(field.getName()));
    }

}
