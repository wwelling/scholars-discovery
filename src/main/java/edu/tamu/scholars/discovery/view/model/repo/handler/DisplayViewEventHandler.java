package edu.tamu.scholars.discovery.view.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.view.model.DisplayView;

@Component("displayViewEventHandler")
@RepositoryEventHandler(DisplayView.class)
public class DisplayViewEventHandler extends ViewEventHandler<DisplayView> {

    public static final String DISPLAY_VIEWS_CHANNEL = "/queue/display-views";

    protected DisplayViewEventHandler(SimpMessagingTemplate simpMessageTemplate) {
        super(simpMessageTemplate);
    }

    @Override
    protected String getChannel() {
        return DISPLAY_VIEWS_CHANNEL;
    }

}
