package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.etl.DataProcessor;
import edu.tamu.scholars.discovery.etl.model.Data;

public interface DataProcessorType<P extends DataProcessor> {

    String[] getRequiredAttributes();

    P getDataProcessor(Data data);

}
