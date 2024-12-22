package edu.tamu.scholars.discovery.config;

import static edu.tamu.scholars.discovery.auth.model.Role.ROLE_ADMIN;
import static edu.tamu.scholars.discovery.auth.model.Role.ROLE_MANAGER;
import static edu.tamu.scholars.discovery.auth.model.Role.ROLE_SUPER_ADMIN;
import static edu.tamu.scholars.discovery.auth.model.Role.ROLE_USER;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RoleHierarchyConfig {

    @Bean
    @NonNull
    RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role(ROLE_SUPER_ADMIN.getValue()).implies(ROLE_ADMIN.getValue())
            .role(ROLE_ADMIN.getValue()).implies(ROLE_MANAGER.getValue())
            .role(ROLE_MANAGER.getValue()).implies(ROLE_USER.getValue())
            .build();
    }

}