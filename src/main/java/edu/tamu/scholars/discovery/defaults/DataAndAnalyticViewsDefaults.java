package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;
import edu.tamu.scholars.discovery.view.model.DataAndAnalyticsView;
import edu.tamu.scholars.discovery.view.model.repo.DataAndAnalyticsViewRepo;

@Service
public class DataAndAnalyticViewsDefaults extends AbstractDefaults<DataAndAnalyticsView, DataAndAnalyticsViewRepo> {

    public DataAndAnalyticViewsDefaults(
            MiddlewareConfig config,
            ResourcePatternResolver resolver,
            DataAndAnalyticsViewRepo repo) {
        super(config, resolver, repo);
    }

    @Override
    public String path() {
        return "classpath:defaults/dataAndAnalyticViews/*.{yml,yaml}";
    }

    @Override
    public List<DataAndAnalyticsView> read(Resource[] resources) throws IOException {
        List<DataAndAnalyticsView> views = loadResources(resources, DataAndAnalyticsView.class);
        for (DataAndAnalyticsView view : views) {
            loadTemplateMap(view.getTemplates());
        }

        return views;
    }

}
