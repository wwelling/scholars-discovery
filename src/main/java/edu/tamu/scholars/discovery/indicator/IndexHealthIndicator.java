package edu.tamu.scholars.discovery.indicator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.factory.index.Index;

@Component
public class IndexHealthIndicator implements HealthIndicator {

    private final Index<?> index;

    IndexHealthIndicator(Index<?> index) {
        this.index = index;
    }

    @Override
    public Health health() {
        Health.Builder status = Health.down();

        Map<String, Object> details = new HashMap<>();

        details.put("collection", this.index.collection());

        if (this.index.ping() == 0) {
            status.up();
        }

        return status.withDetails(details)
            .build();
    }

}