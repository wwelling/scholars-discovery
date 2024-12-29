package edu.tamu.scholars.discovery.etl.extract;

import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.etl.DataProcessor;

public interface DataExtractor<I> extends DataProcessor {

    public Flux<I> extract();

    public I extract(String subject);

}