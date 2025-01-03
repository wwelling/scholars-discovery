package edu.tamu.scholars.discovery.controller;

import static edu.tamu.scholars.discovery.AppConstants.DEFAULT_QUERY;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.model.repo.IndividualRepo;

@RestController
public class IndividualSearchCountController {

    private final IndividualRepo repo;

    IndividualSearchCountController(@Lazy IndividualRepo repo) {
        this.repo = repo;
    }

    @GetMapping(value = "/individual/search/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Count> count(
        @RequestParam(value = "query", required = false, defaultValue = DEFAULT_QUERY) String query,
        List<FilterArg> filters
    ) {
        return ResponseEntity.ok(new Count(repo.count(query, filters)));
    }

    class Count {

        private final long value;

        public Count(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }

    }

}
