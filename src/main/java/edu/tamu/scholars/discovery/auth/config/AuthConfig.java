package edu.tamu.scholars.discovery.auth.config;

import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_REGISTRATION_TOKEN_DURATION;
import static edu.tamu.scholars.discovery.auth.AuthConstants.MIDDLEWARE_AUTH;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration class for authentication-related settings in the Scholars
 * Discovery application. This class manages password policies, token
 * configurations, and registration settings. It is automatically populated by
 * Spring Boot's configuration property binding mechanism using properties
 * prefixed with {@value MIDDLEWARE_AUTH}.
 * 
 * <p>
 * The configuration includes:
 * </p>
 * <ul>
 *   <li>Password policies and validation rules</li>
 *   <li>Authentication token settings</li>
 *   <li>Registration token duration</li>
 * </ul>
 *
 * @see PasswordConfig
 * @see TokenConfig
 * @since 1.5.0
 */
@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(MIDDLEWARE_AUTH)
public class AuthConfig {

    // TODO: add validations and @Validated to the class

    /**
     * Configuration for password validation rules and policies.
     */
    private PasswordConfig password = new PasswordConfig();

    /**
     * Configuration for authentication token settings.
     */
    private TokenConfig token = new TokenConfig();

    /**
     * Duration (in minutes) for which a registration token remains valid.
     * Defaults to {@value DEFAULT_REGISTRATION_TOKEN_DURATION}.
     */
    private int registrationTokenDuration = DEFAULT_REGISTRATION_TOKEN_DURATION;

}
