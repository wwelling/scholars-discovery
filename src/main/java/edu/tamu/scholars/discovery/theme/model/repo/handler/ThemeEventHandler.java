package edu.tamu.scholars.discovery.theme.model.repo.handler;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.tamu.scholars.discovery.messaging.CreateEntityMessage;
import edu.tamu.scholars.discovery.messaging.DeleteEntityMessage;
import edu.tamu.scholars.discovery.messaging.UpdateEntityMessage;
import edu.tamu.scholars.discovery.theme.exception.CreateActiveThemeException;
import edu.tamu.scholars.discovery.theme.exception.DeleteActiveThemeException;
import edu.tamu.scholars.discovery.theme.model.Theme;
import edu.tamu.scholars.discovery.theme.model.repo.ThemeRepo;

@Component("themeEventHandler")
@RepositoryEventHandler(Theme.class)
public class ThemeEventHandler {

    public static final String THEMES_CHANNEL = "/queue/themes";

    private ThemeRepo themeRepo;

    private SimpMessagingTemplate simpMessageTemplate;

    public ThemeEventHandler(ThemeRepo themeRepo, SimpMessagingTemplate simpMessageTemplate) {
        this.themeRepo = themeRepo;
        this.simpMessageTemplate = simpMessageTemplate;
    }

    @HandleBeforeCreate
    public void handleBeforeThemeCreate(Theme theme) throws CreateActiveThemeException {
        if (theme.isActive()) {
            throw new CreateActiveThemeException("You cannot create an active theme!");
        }
    }

    @Transactional
    @HandleBeforeSave
    public void handleBeforeThemeSave(Theme theme) {
        if (theme.isActive()) {
            themeRepo.deactivate();
        }
    }

    @HandleBeforeDelete
    public void handleBeforeThemeDelete(Theme theme) throws DeleteActiveThemeException {
        if (theme.isActive()) {
            throw new DeleteActiveThemeException("You cannot delete the active theme!");
        }
    }

    @HandleAfterCreate
    public void broadcastThemeCreate(Theme theme) {
        simpMessageTemplate.convertAndSend(THEMES_CHANNEL, new CreateEntityMessage<Theme>(theme));
    }

    @HandleAfterSave
    public void broadcastThemeUpdate(Theme theme) {
        simpMessageTemplate.convertAndSend(THEMES_CHANNEL, new UpdateEntityMessage<Theme>(theme));
    }

    @HandleAfterDelete
    public void broadcastThemeDelete(Theme theme) {
        simpMessageTemplate.convertAndSend(THEMES_CHANNEL, new DeleteEntityMessage<String>(theme.getName()));
    }

}
