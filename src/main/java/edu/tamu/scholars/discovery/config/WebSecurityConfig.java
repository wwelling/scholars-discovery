package edu.tamu.scholars.discovery.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.Customizer.withDefaults;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import edu.tamu.scholars.discovery.auth.config.TokenConfig;
import edu.tamu.scholars.discovery.auth.handler.CustomAccessDeniedExceptionHandler;
import edu.tamu.scholars.discovery.auth.handler.CustomAuthenticationEntryPoint;
import edu.tamu.scholars.discovery.auth.handler.CustomAuthenticationFailureHandler;
import edu.tamu.scholars.discovery.auth.handler.CustomAuthenticationSuccessHandler;
import edu.tamu.scholars.discovery.auth.handler.CustomLogoutSuccessHandler;
import edu.tamu.scholars.discovery.config.model.MiddlewareConfig;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    @Value("${spring.profiles.active:default}")
    private String profile;

    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;

    @Value("${server.servlet.session.cookie.domain:library.tamu.edu}")
    private String domainName;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    TokenService tokenService(TokenConfig tokenConfig) throws NoSuchAlgorithmException {
        KeyBasedPersistenceTokenService tokenService = new KeyBasedPersistenceTokenService();
        tokenService.setServerInteger(tokenConfig.getServerInteger());
        tokenService.setServerSecret(tokenConfig.getServerSecret());
        tokenService.setPseudoRandomNumberBytes(tokenConfig.getPseudoRandomNumberBytes());
        tokenService.setSecureRandom(SecureRandom.getInstanceStrong());

        return tokenService;
    }

    @Bean
    CorsFilter corsFilter(MiddlewareConfig config) {
        CorsConfiguration embedConfig = new CorsConfiguration();
        embedConfig.setAllowCredentials(true);
        embedConfig.setAllowedOriginPatterns(Arrays.asList("*"));
        embedConfig.addAllowedHeader("Origin");
        embedConfig.addAllowedHeader("Content-Type");
        embedConfig.addAllowedMethod("GET");
        embedConfig.addAllowedMethod("OPTION");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/displayViews/search/findByName", embedConfig);
        source.registerCorsConfiguration("/individual/{id}", embedConfig);
        source.registerCorsConfiguration("/individual/search/findByIdIn", embedConfig);

        CorsConfiguration primaryConfig = new CorsConfiguration();
        primaryConfig.setAllowCredentials(true);
        primaryConfig.setAllowedOrigins(config.getAllowedOrigins());
        primaryConfig.setAllowedMethods(Arrays.asList(
            "GET",
            "DELETE",
            "OPTIONS",
            "PATCH",
            "PUT",
            "POST"));
        primaryConfig.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Disposition",
            "Content-Type",
            "Origin"));
        primaryConfig.setExposedHeaders(Arrays.asList("Content-Disposition"));

        // NOTE: most general path must be last
        source.registerCorsConfiguration("/**", primaryConfig);
        return new CorsFilter(source);
    }

    @Bean
    LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    @Bean
    CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setUseHttpOnlyCookie(false);
        serializer.setUseSecureCookie(false);
        serializer.setCookiePath("/");
        serializer.setCookieName("SESSION");
        serializer.setDomainName(domainName);

        return serializer;
    }

    @Bean
    MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);

        return expressionHandler;
    }

    @Bean
    protected SecurityFilterChain configure(
            HttpSecurity httpSecurity,
            MessageSource messageSource,
            ObjectMapper objectMapper,
            UserDetailsService userDetailsService) throws Exception {
        if (enableH2Console()) {
            // NOTE: permit all access to h2console
            httpSecurity
                    .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
        }

        httpSecurity
            .authorizeHttpRequests(authorize -> authorize

                .requestMatchers(PATCH,
                    "/dataAndAnalyticsViews/{id}",
                    "/directoryViews/{id}",
                    "/discoveryViews/{id}",
                    "/displayViews/{id}",
                    "/themes/{id}",
                    "/users/{id}")
                    .hasRole("ADMIN")

                .requestMatchers(POST, "/registration")
                    .permitAll()

                .requestMatchers(POST,
                    "/dataAndAnalyticsViews/{id}",
                    "/directoryViews/{id}",
                    "/discoveryViews/{id}",
                    "/displayViews/{id}",
                    "/themes/{id}")
                    .hasRole("ADMIN")

                .requestMatchers(POST, "/users/{id}")
                    .denyAll()

                .requestMatchers(PUT, "/registration")
                    .permitAll()

                .requestMatchers(PUT,
                    "/dataAndAnalyticsViews/{id}",
                    "/directoryViews/{id}",
                    "/discoveryViews/{id}",
                    "/displayViews/{id}",
                    "/themes/{id}")
                    .hasRole("ADMIN")

                .requestMatchers(PUT, "/users/{id}")
                    .denyAll()

                .requestMatchers(GET, "/user")
                    .hasRole("USER")

                .requestMatchers(GET,
                    "/users",
                    "/users/{id}",
                    "/themes",
                    "/themes/{id}")
                    .hasRole("ADMIN")

                .requestMatchers(DELETE,
                    "/dataAndAnalyticsViews/{id}",
                    "/directoryViews/{id}",
                    "/discoveryViews/{id}",
                    "/displayViews/{id}",
                    "/themes/{id}")
                    .hasRole("ADMIN")

                .requestMatchers(DELETE, "/users/{id}")
                    .hasRole("SUPER_ADMIN")

                .anyRequest()
                    .permitAll())

            .formLogin(formLogin -> formLogin
                .successHandler(authenticationSuccessHandler(objectMapper))
                .failureHandler(authenticationFailureHandler())
                    .permitAll())

            .logout(logout -> logout
                .deleteCookies("SESSION")
                .invalidateHttpSession(true)
                .logoutSuccessHandler(logoutSuccessHandler(messageSource))
                    .permitAll())

            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler()))

            .requestCache(requestCache -> requestCache
                .requestCache(nullRequestCache()))

            .cors(withDefaults())

            .csrf(csrf -> csrf.disable());

        httpSecurity
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionFixation(SessionFixationConfigurer::migrateSession)
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return httpSecurity.build();
    }

    private CustomAuthenticationSuccessHandler authenticationSuccessHandler(ObjectMapper objectMapper) {
        return new CustomAuthenticationSuccessHandler(objectMapper);
    }

    private CustomAuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    private CustomLogoutSuccessHandler logoutSuccessHandler(MessageSource messageSource) {
        return new CustomLogoutSuccessHandler(messageSource);
    }

    private CustomAuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    private CustomAccessDeniedExceptionHandler accessDeniedHandler() {
        return new CustomAccessDeniedExceptionHandler();
    }

    private NullRequestCache nullRequestCache() {
        return new NullRequestCache();
    }

    private boolean enableH2Console() {
        return h2ConsoleEnabled && !profile.equals("production");
    }

}
