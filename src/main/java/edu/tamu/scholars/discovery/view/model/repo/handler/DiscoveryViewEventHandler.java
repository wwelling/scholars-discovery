package edu.tamu.scholars.discovery.view.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.view.model.DiscoveryView;

@Component
@RepositoryEventHandler(DiscoveryView.class)
public class DiscoveryViewEventHandler extends ViewEventHandler<DiscoveryView> {

    public static final String DISCOVERY_VIEWS_CHANNEL = "/queue/discovery-views";

    protected DiscoveryViewEventHandler(SimpMessagingTemplate simpMessageTemplate) {
        super(simpMessageTemplate);
    }

    @Override
    protected String getChannel() {
        return DISCOVERY_VIEWS_CHANNEL;
    }

}
