package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.etl.model.Transformer;
import edu.tamu.scholars.discovery.etl.model.repo.TransformerRepo;

@Service
public class TransformerDefaults extends AbstractDefaults<Transformer, TransformerRepo> {

    public TransformerDefaults(
            MiddlewareConfig config,
            ResourcePatternResolver resolver,
            TransformerRepo repo) {
        super(config, resolver, repo);
    }

    @Override
    public String path() {
        return "classpath:defaults/transformers/*.{yml,yaml}";
    }

    @Override
    public List<Transformer> read(Resource[] resources) throws IOException {
        return loadResources(resources, Transformer.class);
    }

}
