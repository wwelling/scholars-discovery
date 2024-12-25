package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.etl.model.Extractor;
import edu.tamu.scholars.discovery.etl.model.repo.ExtractorRepo;

@Service
public class ExtractorDefaults extends AbstractDefaults<Extractor, ExtractorRepo> {

    public ExtractorDefaults(
            MiddlewareConfig config,
            ResourcePatternResolver resolver,
            ExtractorRepo repo) {
        super(config, resolver, repo);
    }

    @Override
    public String path() {
        return "classpath:defaults/extractors/*.{yml,yaml}";
    }

    @Override
    public List<Extractor> read(Resource[] resources) throws IOException {
        return loadResources(resources, Extractor.class);
    }

}
