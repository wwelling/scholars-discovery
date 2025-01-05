package edu.tamu.scholars.discovery.factory.index;

import java.util.List;
import java.util.Optional;

import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.factory.index.dto.IndexQuery;
import edu.tamu.scholars.discovery.model.Individual;

public interface Search {

    List<Individual> query(IndexQuery query);

    Flux<Individual> queryAndStreamResponse(IndexQuery query);

    Optional<Individual> findById(String id);

}
