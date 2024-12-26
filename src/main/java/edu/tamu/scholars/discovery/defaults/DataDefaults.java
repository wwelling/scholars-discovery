package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        updateFields(source, target);

        target.setName(source.getName());
        target.setCollectionSource(source.getCollectionSource());
        target.setFields(source.getFields());
    }

    private void updateFields(Data source, Data target) {
        List<DataField> fields = Optional.ofNullable(source.getFields())
            .orElse(List.of());

        fields.forEach(field -> {
            field.setData(target);

            field.setDescriptor(findOrCreate(field.getDescriptor()));

            List<DataFieldDescriptor> targetDescriptors = Optional.ofNullable(field.getNestedDescriptors())
                .orElse(List.of())
                .stream()
                .map(this::findOrCreate)
                .toList();

            field.setNestedDescriptors(targetDescriptors);
        });
    }

    private DataFieldDescriptor findOrCreate(DataFieldDescriptor sourceDescriptor) {
        return descriptorRepo.findByName(sourceDescriptor.getName())
            .orElseGet(() -> descriptorRepo.save(sourceDescriptor));
    }

}
