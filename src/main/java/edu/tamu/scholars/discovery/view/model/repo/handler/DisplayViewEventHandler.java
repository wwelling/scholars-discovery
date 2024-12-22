package edu.tamu.scholars.discovery.view.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.scholars.discovery.view.model.DisplayView;

/**
 * {@link DisplayView} {@link ViewEventHandler} with const channel
 * defined for handling events.
 */
@RepositoryEventHandler(DisplayView.class)
public class DisplayViewEventHandler extends ViewEventHandler<DisplayView> {

    public static final String DISPLAY_VIEWS_CHANNEL = "/queue/display-views";

    @Override
    protected String getChannel() {
        return DISPLAY_VIEWS_CHANNEL;
    }

}
