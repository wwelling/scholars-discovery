package edu.tamu.scholars.discovery.index.indicator;

import static edu.tamu.scholars.discovery.index.IndexConstants.DEFAULT_QUERY;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.config.model.IndexConfig;
import edu.tamu.scholars.discovery.index.component.solr.SolrService;
import edu.tamu.scholars.discovery.index.model.repo.IndividualRepo;
import edu.tamu.scholars.discovery.index.service.IndexService;

@Component("index")
public class IndexHealthIndicator implements HealthIndicator {

    private final SolrService solrService;

    private final IndividualRepo individualRepo;

    private final IndexService indexService;

    private final IndexConfig index;

    IndexHealthIndicator(
            SolrService solrService,
            IndividualRepo individualRepo,
            IndexService indexService,
            IndexConfig index) {
        this.solrService = solrService;
        this.individualRepo = individualRepo;
        this.indexService = indexService;
        this.index = index;
    }

    @Override
    public Health health() {
        Health.Builder status = Health.down();
        Map<String, Object> details = new HashMap<>();

        details.put("name", index.getName());

        boolean indexing = indexService.isIndexing();

        details.put("indexing", indexing);

        if (!indexing) {
            ResponseEntity<JsonNode> response = solrService.ping();
            HttpStatusCode statusCode = response.getStatusCode();

            details.put("response", response.getBody());

            if (statusCode.is2xxSuccessful()) {
                long count = individualRepo.count(DEFAULT_QUERY, Arrays.asList());

                details.put("count", count);

                status.up();
            }
        }

        return status.withDetails(details)
                .build();
    }

}
