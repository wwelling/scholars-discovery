package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.repo.DataRepo;

@Service
public class DataDefaults extends AbstractDefaults<Data, DataRepo> {

    public DataDefaults(
            MiddlewareConfig config,
            ResourcePatternResolver resolver,
            DataRepo repo) {
        super(config, resolver, repo);
    }

    @Override
    public String path() {
        return "classpath:defaults/data/*.{yml,yaml}";
    }

    @Override
    public List<Data> read(Resource[] resources) throws IOException {
        return loadResources(resources, Data.class);
    }

}
