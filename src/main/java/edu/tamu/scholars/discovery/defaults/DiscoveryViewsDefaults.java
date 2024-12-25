package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
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
        return "classpath:defaults/discoveryViews/*.{yml,yaml}";
    }

    @Override
    public List<DiscoveryView> read(Resource[] resources) throws IOException {
        List<DiscoveryView> views = loadResources(resources, DiscoveryView.class);
        for (DiscoveryView view : views) {
            loadTemplateMap(view.getTemplates());
        }
        return views;
    }

}
