package edu.tamu.scholars.discovery.view.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import edu.tamu.scholars.discovery.view.model.DiscoveryView;

/**
 * {@link DiscoveryView} {@link ViewEventHandler} with const channel
 * defined for handling events.
 */
@RepositoryEventHandler(DiscoveryView.class)
public class DiscoveryViewEventHandler extends ViewEventHandler<DiscoveryView> {

    public static final String DISCOVERY_VIEWS_CHANNEL = "/queue/discovery-views";

    @Override
    protected String getChannel() {
        return DISCOVERY_VIEWS_CHANNEL;
    }

}
