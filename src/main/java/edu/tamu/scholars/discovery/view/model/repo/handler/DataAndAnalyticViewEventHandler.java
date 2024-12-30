package edu.tamu.scholars.discovery.view.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.scholars.discovery.view.model.DataAndAnalyticsView;

@RepositoryEventHandler(DataAndAnalyticsView.class)
public class DataAndAnalyticViewEventHandler extends ViewEventHandler<DataAndAnalyticsView> {

    public static final String DATA_AND_ANALYTICS_VIEWS_CHANNEL = "/queue/data-and-analytics-views";

    protected DataAndAnalyticViewEventHandler(SimpMessagingTemplate simpMessageTemplate) {
        super(simpMessageTemplate);
    }

    @Override
    protected String getChannel() {
        return DATA_AND_ANALYTICS_VIEWS_CHANNEL;
    }

}
