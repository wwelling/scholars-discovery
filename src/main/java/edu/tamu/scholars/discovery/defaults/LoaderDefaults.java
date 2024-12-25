package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.etl.model.Loader;
import edu.tamu.scholars.discovery.etl.model.repo.LoaderRepo;

@Service
public class LoaderDefaults extends AbstractDefaults<Loader, LoaderRepo> {

    public LoaderDefaults(
            MiddlewareConfig config,
            ResourcePatternResolver resolver,
            LoaderRepo repo) {
        super(config, resolver, repo);
    }

    @Override
    public String path() {
        return "classpath:defaults/loaders/*.{yml,yaml}";
    }

    @Override
    public List<Loader> read(Resource[] resources) throws IOException {
        return loadResources(resources, Loader.class);
    }

}
