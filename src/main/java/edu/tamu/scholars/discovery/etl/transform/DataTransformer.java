package edu.tamu.scholars.discovery.etl.transform;

import edu.tamu.scholars.discovery.etl.DataProcessor;

public interface DataTransformer<I, O> extends DataProcessor {

    public O transform(I data);

}
