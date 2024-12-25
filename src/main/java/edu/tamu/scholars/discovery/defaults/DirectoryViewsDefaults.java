package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.view.model.DirectoryView;
import edu.tamu.scholars.discovery.view.model.repo.DirectoryViewRepo;

@Service
public class DirectoryViewsDefaults extends AbstractDefaults<DirectoryView, DirectoryViewRepo> {

    public DirectoryViewsDefaults(
        MiddlewareConfig config,
        ResourcePatternResolver resolver,
        DirectoryViewRepo repo
    ) {
        super(config, resolver, repo);
    }

    @Override
    public String path() {
        return "classpath:defaults/directoryViews/*.{yml,yaml}";
    }

    @Override
    public List<DirectoryView> read(Resource[] resources) throws IOException {
        List<DirectoryView> views = loadResources(resources, DirectoryView.class);
        for (DirectoryView view : views) {
            loadTemplateMap(view.getTemplates());
        }

        return views;
    }

}
