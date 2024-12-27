package edu.tamu.scholars.discovery.etl.model.repo.handler;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.scholars.discovery.etl.model.Data;

@RepositoryEventHandler(Data.class)
public class DataHandler {

    @HandleBeforeCreate
    public void validateBeforeCreate(Data data) {
        validateRequiredAttribute(data);
    }

    @HandleBeforeSave
    public void validateBeforeSave(Data data) {
        validateRequiredAttribute(data);
    }

    public void validateRequiredAttribute(Data data) {
        // validate data before create or save
        // validate extractor, transformer, and loader are all compatible
    }

}
