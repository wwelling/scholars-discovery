package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.view.model.DataAndAnalyticsView;
import edu.tamu.scholars.discovery.view.model.repo.DataAndAnalyticsViewRepo;

@Service
public class AnalyticViewsDefaults extends AbstractDefaults<DataAndAnalyticsView, DataAndAnalyticsViewRepo> {

    public AnalyticViewsDefaults() {
        super();
    }

    @Override
    public String path() {
        return "classpath:defaults/dataAndanalyticViews.yml";
    }

    @Override
    public List<DataAndAnalyticsView> read(InputStream is) throws IOException {
        List<DataAndAnalyticsView> views = mapper.readValue(is, new TypeReference<List<DataAndAnalyticsView>>() {});
        for (DataAndAnalyticsView view : views) {
            loadTemplateMap(view.getTemplates());
        }
        return views;
    }

}
