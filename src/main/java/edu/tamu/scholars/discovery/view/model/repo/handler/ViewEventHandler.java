package edu.tamu.scholars.discovery.view.model.repo.handler;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.scholars.discovery.messaging.CreateEntityMessage;
import edu.tamu.scholars.discovery.messaging.DeleteEntityMessage;
import edu.tamu.scholars.discovery.messaging.UpdateEntityMessage;
import edu.tamu.scholars.discovery.view.model.View;

public abstract class ViewEventHandler<V extends View> {

    private final SimpMessagingTemplate simpMessageTemplate;

    protected ViewEventHandler(SimpMessagingTemplate simpMessageTemplate) {
        this.simpMessageTemplate = simpMessageTemplate;
    }

    @HandleAfterCreate
    public void broadcastCreate(V view) {
        simpMessageTemplate.convertAndSend(getChannel(), new CreateEntityMessage<V>(view));
    }

    @HandleAfterSave
    public void broadcastUpdate(V view) {
        simpMessageTemplate.convertAndSend(getChannel(), new UpdateEntityMessage<V>(view));
    }

    @HandleAfterDelete
    public void broadcastDelete(V view) {
        simpMessageTemplate.convertAndSend(getChannel(), new DeleteEntityMessage<String>(view.getName()));
    }

    protected abstract String getChannel();

}
