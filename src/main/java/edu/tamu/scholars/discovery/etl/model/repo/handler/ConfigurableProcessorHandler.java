package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;

import edu.tamu.scholars.discovery.etl.model.ConfigurableProcessor;
import edu.tamu.scholars.discovery.etl.model.DataProcessorType;

public abstract class ConfigurableProcessorHandler<T extends DataProcessorType, P extends ConfigurableProcessor<T>> {

    @HandleBeforeCreate
    public void validateBeforeCreate(P processor) {
        validateRequiredAttribute(processor);
    }

    @HandleBeforeSave
    public void validateBeforeSave(P processor) {
        validateRequiredAttribute(processor);
    }

    public void validateRequiredAttribute(P processor) {
        // validate configurable processor before create or save
        // check configurable processor attributes have all required attributes from enum
    }

}
