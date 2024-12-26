package edu.tamu.scholars.discovery.defaults.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.defaults.Defaults;

@Service
public class DefaultsService {

    private final MiddlewareConfig discovery;

    private final List<Defaults<?>> defaults;

    DefaultsService(MiddlewareConfig discovery, List<Defaults<?>> defaults) {
        this.discovery = discovery;
        this.defaults = defaults;
    }

    @PostConstruct
    public void load() throws IOException {
        if (discovery.isLoadDefaults()) {

            final Set<Class<?>> loadedDefaults = new HashSet<>();

            while (!defaults.isEmpty()) {

                boolean progress = false;

                for (Defaults<?> service : defaults) {
                    boolean dependenciesLoaded = areDependenciesLoaded(service, loadedDefaults);

                    if (dependenciesLoaded) {
                        service.load();
                        loadedDefaults.add(service.getClass());
                        defaults.remove(service);
                        progress = true;
                        break;
                    }
                }

                if (!progress) {
                    throw new IllegalStateException("Cyclic dependencies detected or unable to resolve all defaults.");
                }

            }
        }
    }

    private boolean areDependenciesLoaded(Defaults<?> service, Set<Class<?>> loadedDefaults) {
        boolean dependenciesLoaded = true;

        for (Class<?> defaultsClass : service.getDefaultDependencies()) {
            if (!loadedDefaults.contains(defaultsClass)) {
                dependenciesLoaded = false;
            }
        }

        return dependenciesLoaded;
    }

}
