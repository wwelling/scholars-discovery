package edu.tamu.scholars.discovery.etl.model.repo.listener;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.repo.DataFieldDescriptorRepo;

@Slf4j
@Component
public class DataEntityListener {

    private final DataFieldDescriptorRepo descriptorRepo;

    public DataEntityListener(@Lazy DataFieldDescriptorRepo descriptorRepo) {
        this.descriptorRepo = descriptorRepo;
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
        // validate extractor, transformer, and loader are all compatible
        // validate descriptors name/reference key do not have conflicting field destinations

        List<DataFieldDescriptor> existingDescriptors = descriptorRepo.findAll();

        data.getFields()
            .stream()
            .map(DataField::getDescriptor)
            .forEach(currentDescriptor -> validateDescriptor(currentDescriptor, existingDescriptors));

    }

    public void validateDescriptor(DataFieldDescriptor currentDescriptor, List<DataFieldDescriptor> existingDescriptors) {
        for (DataFieldDescriptor existingDescriptor : existingDescriptors) {
            validateDescriptor(currentDescriptor, existingDescriptor);
        }
        for (DataFieldDescriptor currentNestedDescriptor : currentDescriptor.getNestedDescriptors()) {
            validateDescriptor(currentNestedDescriptor, existingDescriptors);
        }
    }

    public void validateDescriptor(DataFieldDescriptor currentDescriptor, DataFieldDescriptor existingDescriptor) {
        String currentDescriptorFieldName = getFieldName(currentDescriptor);
        String existingDescriptorFieldName = getFieldName(existingDescriptor);
        if (currentDescriptorFieldName.equals(existingDescriptorFieldName) && !currentDescriptor.getDestination().equals(existingDescriptor.getDestination())) {
            log.error("Conflicting descriptors {} != {}", currentDescriptor, existingDescriptor);
        }
    }

    public String getFieldName(DataFieldDescriptor descriptor) {
        return descriptor.getNestedReference() != null
            && isNotEmpty(descriptor.getNestedReference().getKey())
                ? descriptor.getNestedReference().getKey()
                : descriptor.getName();
    }

}
