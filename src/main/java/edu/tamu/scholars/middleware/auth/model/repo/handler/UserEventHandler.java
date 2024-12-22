package edu.tamu.scholars.middleware.auth.model.repo.handler;

import static edu.tamu.scholars.middleware.auth.AuthConstants.USERS_CHANNEL;

import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.scholars.middleware.auth.model.User;
import edu.tamu.scholars.middleware.messaging.DeleteEntityMessage;
import edu.tamu.scholars.middleware.messaging.UpdateEntityMessage;

@RepositoryEventHandler(User.class)
public class UserEventHandler {

    private final SimpMessagingTemplate simpMessageTemplate;

    public UserEventHandler(SimpMessagingTemplate simpMessageTemplate) {
        this.simpMessageTemplate = simpMessageTemplate;
    }

    @HandleAfterSave
    public void broadcastUserUpdate(User user) {
        simpMessageTemplate.convertAndSend(
            USERS_CHANNEL,
            new UpdateEntityMessage<User>(user));
        simpMessageTemplate.convertAndSendToUser(
            user.getEmail(),
            USERS_CHANNEL,
            new UpdateEntityMessage<User>(user));
    }

    @HandleAfterDelete
    public void broadcastUserDelete(User user) {
        simpMessageTemplate.convertAndSend(
            USERS_CHANNEL,
            new DeleteEntityMessage<String>(user.getEmail()));
        simpMessageTemplate.convertAndSendToUser(
            user.getEmail(),
            USERS_CHANNEL,
            new DeleteEntityMessage<String>(user.getEmail()));
    }

}
