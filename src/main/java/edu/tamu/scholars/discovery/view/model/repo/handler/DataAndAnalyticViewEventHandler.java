package edu.tamu.scholars.discovery.view.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.scholars.discovery.view.model.DataAndAnalyticsView;

/**
 * {@link DataAndAnalyticsView} {@link ViewEventHandler} with const channel
 * defined for handling events.
 */
@RepositoryEventHandler(DataAndAnalyticsView.class)
public class DataAndAnalyticViewEventHandler extends ViewEventHandler<DataAndAnalyticsView> {

    public static final String DATA_AND_ANALYTICS_VIEWS_CHANNEL = "/queue/data-and-analytics-views";

    @Override
    protected String getChannel() {
        return DATA_AND_ANALYTICS_VIEWS_CHANNEL;
    }

}
