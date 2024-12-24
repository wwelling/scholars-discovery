package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;

@Service
public class DefaultsService {

    private final MiddlewareConfig discovery;
    private final List<Defaults<?, ?>> defaults;

    DefaultsService(MiddlewareConfig discovery, List<Defaults<?, ?>> defaults) {
        this.discovery = discovery;
        this.defaults = defaults;
    }

    @PostConstruct
    public void init() throws IOException {
        if (discovery.isLoadDefaults()) {
            for (Defaults<?, ?> service : defaults) {
                service.load();
            }
        }
    }

}
