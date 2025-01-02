package edu.tamu.scholars.discovery.etl.model.repo.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

import edu.tamu.scholars.discovery.etl.DataProcessor;
import edu.tamu.scholars.discovery.etl.model.ConfigurableProcessor;
import edu.tamu.scholars.discovery.etl.model.type.DataProcessorType;

@Slf4j
public abstract class ConfigurableProcessorEntityListener<P extends DataProcessor, T extends DataProcessorType<P>, C extends ConfigurableProcessor<P, T>> {

    @PrePersist
    public void validatePrePersist(C processor) {
        log.info("Validating {} {} before persisting", processor.getClass().getSimpleName(), processor.getName());
    }

    @PreUpdate
    public void validatePreUpdate(C processor) {
        log.info("Validating {} {} before updating", processor.getClass().getSimpleName(), processor.getName());
    }

    public void validate(C processor) {
        // validate configurable processor before create or save
        // validate required attributes from enum
    }

}
