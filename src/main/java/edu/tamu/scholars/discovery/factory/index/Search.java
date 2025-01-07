package edu.tamu.scholars.discovery.factory.index;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.factory.index.dto.IndexQuery;
import edu.tamu.scholars.discovery.model.Individual;

public interface Search {

    List<Individual> query(IndexQuery query);

    Flux<Individual> queryAndStreamResponse(IndexQuery query);

    Optional<Individual> findById(String id);

    List<Individual> findByIdIn(
        List<String> ids,
        List<FilterArg> filters,
        Sort sort,
        int limit
    );

}
