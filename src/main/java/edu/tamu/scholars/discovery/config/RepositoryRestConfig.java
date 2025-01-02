package edu.tamu.scholars.discovery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
@EnableJpaRepositories(basePackages = "edu.tamu.scholars")
public class RepositoryRestConfig implements RepositoryRestConfigurer {

    @Bean
    PageableHandlerMethodArgumentResolverCustomizer customize() {
        return resolver -> resolver.setOneIndexedParameters(true);
    }

}
