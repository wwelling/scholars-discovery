package edu.tamu.scholars.discovery.etl.extract;

import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.etl.DataProcessor;

public interface DataExtractor<O> extends DataProcessor {

    public Flux<O> extract();

    public O extract(String subject);

}