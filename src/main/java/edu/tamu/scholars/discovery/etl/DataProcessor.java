package edu.tamu.scholars.discovery.etl;

import java.util.List;

import edu.tamu.scholars.discovery.etl.model.DataField;

public interface DataProcessor {

    public void init();

    public void preProcess(List<DataField> fields);

    public void postProcess(List<DataField> fields);

    public void destroy();

}