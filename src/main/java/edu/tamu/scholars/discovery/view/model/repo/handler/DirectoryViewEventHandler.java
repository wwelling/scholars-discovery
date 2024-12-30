package edu.tamu.scholars.discovery.view.model.repo.handler;

import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.scholars.discovery.view.model.DirectoryView;

@RepositoryEventHandler(DirectoryView.class)
public class DirectoryViewEventHandler extends ViewEventHandler<DirectoryView> {

    public static final String DIRECTORY_VIEWS_CHANNEL = "/queue/directory-views";

    protected DirectoryViewEventHandler(SimpMessagingTemplate simpMessageTemplate) {
        super(simpMessageTemplate);
    }

    @Override
    protected String getChannel() {
        return DIRECTORY_VIEWS_CHANNEL;
    }

}
