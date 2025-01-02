package edu.tamu.scholars.discovery.auth.model.repo.handler;

import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_CHANNEL;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.auth.model.User;
import edu.tamu.scholars.discovery.messaging.DeleteEntityMessage;
import edu.tamu.scholars.discovery.messaging.UpdateEntityMessage;

@Slf4j
@Component
@RepositoryEventHandler(User.class)
public class UserEventHandler {

    private final SimpMessagingTemplate simpMessageTemplate;

    public UserEventHandler(SimpMessagingTemplate simpMessageTemplate) {
        this.simpMessageTemplate = simpMessageTemplate;
    }

    @HandleAfterSave
    public void broadcastUserUpdate(User user) {
        log.info("Broadcasting user with id {} after update.", user.getId());
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
        log.info("Broadcasting user email for user with id {} after delete.", user.getId());
        simpMessageTemplate.convertAndSend(
            USERS_CHANNEL,
            new DeleteEntityMessage<String>(user.getEmail()));
        simpMessageTemplate.convertAndSendToUser(
            user.getEmail(),
            USERS_CHANNEL,
            new DeleteEntityMessage<String>(user.getEmail()));
    }

}
