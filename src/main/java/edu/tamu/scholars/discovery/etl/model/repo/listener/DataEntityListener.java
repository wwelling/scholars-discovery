package edu.tamu.scholars.discovery.etl.model.repo.listener;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.etl.DataProcessor;
import edu.tamu.scholars.discovery.etl.model.ConfigurableProcessor;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.Extractor;
import edu.tamu.scholars.discovery.etl.model.Loader;
import edu.tamu.scholars.discovery.etl.model.Transformer;
import edu.tamu.scholars.discovery.etl.model.repo.DataRepo;
import edu.tamu.scholars.discovery.etl.model.repo.ExtractorRepo;
import edu.tamu.scholars.discovery.etl.model.repo.LoaderRepo;
import edu.tamu.scholars.discovery.etl.model.repo.TransformerRepo;

@Slf4j
@Component
public class DataEntityListener {

    private final DataRepo dataRepo;

    private final ExtractorRepo extractorRepo;

    private final TransformerRepo transformerRepo;

    private final LoaderRepo loaderRepo;

    public DataEntityListener(
            @Lazy DataRepo dataRepo,
            @Lazy ExtractorRepo extractorRepo,
            @Lazy TransformerRepo transformerRepo,
            @Lazy LoaderRepo loaderRepo) {
        this.dataRepo = dataRepo;
        this.extractorRepo = extractorRepo;
        this.transformerRepo = transformerRepo;
        this.loaderRepo = loaderRepo;
    }

    @PrePersist
    public void validatePrePersist(Data data) {
        log.info("Validating Data {} before persisting", data.getName());
        validate(data);
    }

    @PreUpdate
    public void validatePreUpdate(Data data) {
        log.info("Validating Data {} before updating", data.getName());
        validate(data);
    }

    public void validate(Data data) {
        // TODO: create custom exception and throw when not valid
        try {
            validateProcessors(data);
        } catch (SecurityException e) {
            log.error("Error validating processors", e);
        }
        validateDescriptors(data);
    }

    private void validateProcessors(Data data) throws SecurityException {
        Long extractorId = data.getExtractor().getId();
        Long transformerId = data.getTransformer().getId();
        Long loaderId = data.getLoader().getId();

        Optional<Extractor> extractor = extractorRepo.findById(extractorId);
        Optional<Transformer> transformer = transformerRepo.findById(transformerId);
        Optional<Loader> loader = loaderRepo.findById(loaderId);

        if (extractor.isPresent() && transformer.isPresent() && loader.isPresent()) {
            Type[] extractorTypes = getProcessorTypeArguments(extractor.get(), data);
            Type[] transformerTypes = getProcessorTypeArguments(transformer.get(), data);
            Type[] loaderTypes = getProcessorTypeArguments(loader.get(), data);

            if (extractorTypes.length > 0 && transformerTypes.length > 1 && loaderTypes.length > 0) {
                if (!extractorTypes[0].equals(transformerTypes[0])) {
                    log.error("Extractor type does not match transformer input type.");
                }
                if (!transformerTypes[1].equals(loaderTypes[0])) {
                    log.error("Transformer output type does not match loader type.");
                }
            } else {
                log.error("Processors do not have the expected generic types.");
            }
        } else {
            log.error("Processors not found on data.");
        }
    }

    private <C extends ConfigurableProcessor<?, ?>> Type[] getProcessorTypeArguments(C cp, Data data) {
        DataProcessor processor = cp.getType().getDataProcessor(data);
        Class<?> currentClass = processor.getClass();
        while (currentClass != null) {
            Type[] genericInterfaces = currentClass.getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType paramType) {
                    return paramType.getActualTypeArguments();
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        processor.destroy();

        return new Type[0];
    }

    private void validateDescriptors(Data data) {
        List<DataFieldDescriptor> existingDescriptors = dataRepo.findByLoaderId(data.getLoader().getId())
            .stream()
            .flatMap(datum -> datum.getFields().stream())
            .map(DataField::getDescriptor)
            .distinct()
            .toList();

        for (DataField field : data.getFields()) {
            validateDescriptor(field.getDescriptor(), existingDescriptors);
        }
    }

    private void validateDescriptor(DataFieldDescriptor currentDescriptor,
        List<DataFieldDescriptor> existingDescriptors) {
        for (DataFieldDescriptor existingDescriptor : existingDescriptors) {
            validateDescriptor(currentDescriptor, existingDescriptor);
        }
        for (DataFieldDescriptor currentNestedDescriptor : currentDescriptor.getNestedDescriptors()) {
            validateDescriptor(currentNestedDescriptor, existingDescriptors);
        }
    }

    private void validateDescriptor(DataFieldDescriptor currentDescriptor, DataFieldDescriptor existingDescriptor) {
        String currentDescriptorFieldName = getFieldName(currentDescriptor);
        String existingDescriptorFieldName = getFieldName(existingDescriptor);

        if (currentDescriptorFieldName.equals(existingDescriptorFieldName)
            && !currentDescriptor.getDestination().equals(existingDescriptor.getDestination())) {
            log.error("Conflicting descriptor destinations \n{} {} \n{} {}",
                currentDescriptorFieldName,
                currentDescriptor.getDestination(),
                existingDescriptorFieldName,
                existingDescriptor.getDestination());
        }
    }

    private String getFieldName(DataFieldDescriptor descriptor) {
        return descriptor.getNestedReference() != null
            && isNotEmpty(descriptor.getNestedReference().getKey())
                ? descriptor.getNestedReference().getKey()
                : descriptor.getName();
    }

}
