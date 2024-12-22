package edu.tamu.scholars.middleware.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import edu.tamu.scholars.middleware.auth.model.repo.handler.UserEventHandler;
import edu.tamu.scholars.middleware.theme.model.repo.ThemeRepo;
import edu.tamu.scholars.middleware.theme.model.repo.handler.ThemeEventHandler;

/**
 * 
 */
@Configuration
public class RepositoryRestConfig implements RepositoryRestConfigurer {

    @Bean
    PageableHandlerMethodArgumentResolverCustomizer customize() {
        return resolver -> resolver.setOneIndexedParameters(true);
    }

    @Bean
    ThemeEventHandler themeEventHandler(
        ThemeRepo themeRepo,
        SimpMessagingTemplate simpMessageTemplate
    ) {
        return new ThemeEventHandler(themeRepo, simpMessageTemplate);
    }

    @Bean
    UserEventHandler userEventHandler(SimpMessagingTemplate simpMessageTemplate) {
        return new UserEventHandler(simpMessageTemplate);
    }

}
