package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.component.Service;
import edu.tamu.scholars.discovery.etl.DataProcessor;
import edu.tamu.scholars.discovery.etl.model.Data;

public interface DataProcessorType<P extends DataProcessor, S extends Service> {

    String[] getRequiredAttributes();

    P getDataProcessor(Data data, S service);

}
