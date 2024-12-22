package edu.tamu.scholars.middleware.defaults;

import java.io.IOException;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.middleware.config.model.MiddlewareConfig;

/**
 * 
 */
@Service
public class DefaultsService {

    @Autowired
    private MiddlewareConfig middleware;

    @Autowired
    private List<Defaults<?, ?>> defaults;

    @PostConstruct
    public void init() throws IOException {
        if (middleware.isLoadDefaults()) {
            for (Defaults<?, ?> service : defaults) {
                service.load();
            }
        }
    }

}
