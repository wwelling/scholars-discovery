package edu.tamu.scholars.discovery.etl.model.repo.handler;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.repo.DataFieldDescriptorRepo;

@Slf4j
@Component
@RepositoryEventHandler(Data.class)
public class DataEventHandler {

    private final DataFieldDescriptorRepo descriptorRepo;

    public DataEventHandler(DataFieldDescriptorRepo descriptorRepo) {
        this.descriptorRepo = descriptorRepo;
    }

    @HandleBeforeCreate
    public void validateBeforeCreate(Data data) {
        log.info("Validating {} data before create", data.getName());
        validate(data);
    }

    @HandleBeforeSave
    public void validateBeforeSave(Data data) {
        log.info("Validating {} data before save", data.getName());
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
        log.info("Validating {} descriptor before save", currentDescriptor.getName());
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
            String message = format("Conflicting descriptors %s != %s", currentDescriptor, existingDescriptor);
            log.error("Conflicting descriptors {} != {}", currentDescriptor, existingDescriptor);

            throw new RuntimeException(message);
        }
    }

    public String getFieldName(DataFieldDescriptor descriptor) {
        return descriptor.getNestedReference() != null
            && isNotEmpty(descriptor.getNestedReference().getKey())
                ? descriptor.getNestedReference().getKey()
                : descriptor.getName();
    }

}
