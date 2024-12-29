package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;

import edu.tamu.scholars.discovery.component.Service;
import edu.tamu.scholars.discovery.etl.DataProcessor;
import edu.tamu.scholars.discovery.etl.model.ConfigurableProcessor;
import edu.tamu.scholars.discovery.etl.model.type.DataProcessorType;

public abstract class ConfigurableProcessorHandler<P extends DataProcessor, S extends Service, T extends DataProcessorType<P, S>, C extends ConfigurableProcessor<P, S, T>> {

    @HandleBeforeCreate
    public void validateBeforeCreate(C processor) {
        validateRequiredAttribute(processor);
    }

    @HandleBeforeSave
    public void validateBeforeSave(C processor) {
        validateRequiredAttribute(processor);
    }

    public void validateRequiredAttribute(C processor) {
        // validate configurable processor before create or save
        // check configurable processor attributes have all required attributes from enum
    }

}
