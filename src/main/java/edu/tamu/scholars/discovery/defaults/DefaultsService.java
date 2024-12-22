package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;

/**
 * 
 */
@Service
public class DefaultsService {

    @Autowired
    private MiddlewareConfig discovery;

    @Autowired
    private List<Defaults<?, ?>> defaults;

    @PostConstruct
    public void init() throws IOException {
        if (discovery.isLoadDefaults()) {
            for (Defaults<?, ?> service : defaults) {
                service.load();
            }
        }
    }

}
