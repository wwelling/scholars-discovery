package edu.tamu.scholars.discovery.config;

import static org.springframework.messaging.simp.SimpMessageType.CONNECT;
import static org.springframework.messaging.simp.SimpMessageType.DISCONNECT;
import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;
import static org.springframework.messaging.simp.SimpMessageType.UNSUBSCRIBE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.expression.DefaultMessageSecurityExpressionHandler;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
public class WebSocketSecurityConfig {

    @Bean
    DefaultMessageSecurityExpressionHandler<Message<Object>> messageSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMessageSecurityExpressionHandler<Message<Object>> expressionHandler = new DefaultMessageSecurityExpressionHandler<>();
        expressionHandler.setRoleHierarchy(roleHierarchy);

        return expressionHandler;
    }

    @Bean
    @SuppressWarnings("java:S1452")
    AuthorizationManager<Message<?>> messageAuthorizationManager() {
        return MessageMatcherDelegatingAuthorizationManager.builder()
            .simpTypeMatchers(CONNECT, UNSUBSCRIBE, DISCONNECT)
                .permitAll()
            .simpSubscribeDestMatchers(
                    "/queue/public",
                    "/queue/themes",
                    "/queue/dataAndAnalyticsViews",
                    "/queue/directoryViews",
                    "/queue/discoveryViews",
                    "/queue/displayViews",
                    "/queue/collections",
                    "/queue/concepts",
                    "/queue/documents",
                    "/queue/organizations",
                    "/queue/persons",
                    "/queue/processes",
                    "/queue/relationships",
                    "/queue/individual"
                )
                .permitAll()
            .simpSubscribeDestMatchers("/user/queue/users")
                .hasRole("USER")
            .simpSubscribeDestMatchers("/queue/users")
                .hasRole("ADMIN")
            .simpTypeMatchers(SUBSCRIBE, MESSAGE)
                .denyAll()
            .anyMessage()
                .denyAll()
            .build();
    }

}
