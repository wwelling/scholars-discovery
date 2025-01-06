package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.etl.model.CollectionSource;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.Source;
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
        List<Data> data = loadResources(resources, Data.class);
        for (Data datum : data) {
            CollectionSource collectionSource = datum.getCollectionSource();
            if (collectionSource.getTemplate() != null && !collectionSource.getTemplate().isEmpty()) {
                try {
                    collectionSource.setTemplate(getTemplate(collectionSource.getTemplate()));
                } catch (IOException e) {
                    logger.warn(String.format(IO_EXCEPTION_MESSAGE, collectionSource.getTemplate()));
                }
            }
            datum.getFields()
                .stream()
                .map(DataField::getDescriptor)
                .forEach(this::setSourceTemplates);
        }

        return data;
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

    private void setSourceTemplates(DataFieldDescriptor descriptor) {
        setSourceTemplate(descriptor.getSource());
        for (Source source : descriptor.getSource().getCacheableSources()) {
            setSourceTemplate(source);
        }
        descriptor.getNestedDescriptors().forEach(this::setSourceTemplates);
    }

    private <S extends Source> void setSourceTemplate(S source) {
        try {
            source.setTemplate(getTemplate(source.getTemplate()));
        } catch (IOException e) {
            logger.warn(String.format(IO_EXCEPTION_MESSAGE, source.getTemplate()));
        }
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

                return targetField;
            }).collect(Collectors.toSet());

        target.setFields(updatedFields);
    }

    private DataFieldDescriptor resolveDescriptor(DataFieldDescriptor descriptor) {

        String key = Objects.nonNull(descriptor.getNestedReference())
            ? descriptor.getNestedReference().getKey()
            : null;

        Specification<DataFieldDescriptor> specification = (root, query, cb) -> cb.and(
            cb.equal(root.get("name"), descriptor.getName()),
            cb.equal(root.get("nested"), descriptor.isNested()),
            cb.equal(root.get("destination").get("type"), descriptor.getDestination().getType()),
            cb.equal(root.get("destination").get("defaultValue"), descriptor.getDestination().getDefaultValue()),
            cb.equal(root.get("destination").get("required"), descriptor.getDestination().isRequired()),
            cb.equal(root.get("destination").get("stored"), descriptor.getDestination().isStored()),
            cb.equal(root.get("destination").get("indexed"), descriptor.getDestination().isIndexed()),
            cb.equal(root.get("destination").get("multiValued"), descriptor.getDestination().isMultiValued()),
            cb.equal(root.get("destination").get("docValues"), descriptor.getDestination().isDocValues()),
            cb.equal(root.get("source").get("predicate"), descriptor.getSource().getPredicate()),
            cb.equal(root.get("source").get("template"), descriptor.getSource().getTemplate()),
            cb.equal(root.get("source").get("unique"), descriptor.getSource().isUnique()),
            cb.equal(root.get("source").get("parse"), descriptor.getSource().isParse()),
            cb.equal(root.get("source").get("split"), descriptor.getSource().isSplit()),
            cb.equal(root.get("nestedReference").get("key"), key));

        return descriptorRepo.findOne(specification)
            .map(entity -> descriptorRepo.findById(entity.getId()).orElse(entity))
            .filter(existing -> isEquivalent(existing, descriptor))
            .orElseGet(() -> descriptorRepo.save(descriptor));
    }

    private boolean isEquivalent(DataFieldDescriptor existing, DataFieldDescriptor descriptor) {
        return existing.getDestination().equals(descriptor.getDestination())
            && existing.getSource().equals(descriptor.getSource())
            && existing.getNestedReference().equals(descriptor.getNestedReference())
            && existing.getNestedDescriptors().equals(descriptor.getNestedDescriptors());
     }

}
