package edu.tamu.scholars.discovery.discovery.indicator;

import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.DEFAULT_QUERY;

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
import edu.tamu.scholars.discovery.discovery.model.repo.IndividualRepo;
import edu.tamu.scholars.discovery.discovery.service.SolrService;

@Component("index")
public class IndexHealthIndicator implements HealthIndicator {

    private final SolrService solrService;

    private final IndividualRepo individualRepo;

    private final IndexConfig index;

    IndexHealthIndicator(
            SolrService solrService,
            IndividualRepo individualRepo,
            IndexConfig index) {
        this.solrService = solrService;
        this.individualRepo = individualRepo;
        this.index = index;
    }

    @Override
    public Health health() {
        Health.Builder status = Health.down();
        Map<String, Object> details = new HashMap<>();
        ResponseEntity<JsonNode> response = solrService.ping();
        HttpStatusCode statusCode = response.getStatusCode();

        details.put("name", index.getName());
        details.put("response", response.getBody());

        if (statusCode.is2xxSuccessful()) {
            // long count = individualRepo.count(DEFAULT_QUERY, Arrays.asList());
            // details.put("count", count);

            status.up();
        }

        return status.withDetails(details)
            .build();
    }

}
