package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.view.model.DiscoveryView;
import edu.tamu.scholars.discovery.view.model.repo.DiscoveryViewRepo;

@Service
public class DiscoveryViewsDefaults extends AbstractDefaults<DiscoveryView, DiscoveryViewRepo> {

    public DiscoveryViewsDefaults(
        MiddlewareConfig config,
        ResourcePatternResolver resolver,
        DiscoveryViewRepo repo
    ) {
        super(config, resolver, repo);
    }

    @Override
    public String path() {
        return "classpath:defaults/discoveryViews.yml";
    }

    @Override
    public List<DiscoveryView> read(InputStream is) throws IOException {
        List<DiscoveryView> views = mapper.readValue(is, new TypeReference<List<DiscoveryView>>() {});
        for (DiscoveryView view : views) {
            loadTemplateMap(view.getTemplates());
        }
        return views;
    }

}
