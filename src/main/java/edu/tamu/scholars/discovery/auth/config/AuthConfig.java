package edu.tamu.scholars.discovery.auth.config;

import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_REGISTRATION_TOKEN_DURATION;
import static edu.tamu.scholars.discovery.auth.AuthConstants.MIDDLEWARE_AUTH;

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
@Component
@ConfigurationProperties(MIDDLEWARE_AUTH)
public class AuthConfig {

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

    /**
     * Default constructor required for Spring's configuration property binding.
     * This constructor is called via reflection by Spring's configuration
     * mechanism.
     */
    public AuthConfig() {
        // this configuration class is instantiated by reflection by Spring
    }

    /**
     * Retrieves the password configuration settings.
     *
     * @return the current password configuration
     */
    public PasswordConfig getPassword() {
        return password;
    }

    /**
     * Sets the password configuration settings.
     *
     * @param password the password configuration to set
     */
    public void setPassword(PasswordConfig password) {
        this.password = password;
    }

    /**
     * Retrieves the token configuration settings.
     *
     * @return the current token configuration
     */
    public TokenConfig getToken() {
        return token;
    }

    /**
     * Sets the token configuration settings.
     *
     * @param token the token configuration to set
     */
    public void setToken(TokenConfig token) {
        this.token = token;
    }

    /**
     * Retrieves the registration token duration.
     *
     * @return the duration in minutes for which a registration token remains valid
     */
    public int getRegistrationTokenDuration() {
        return registrationTokenDuration;
    }

    /**
     * Sets the registration token duration.
     *
     * @param registrationTokenDuration the duration in minutes for which a
     *                                  registration token should remain valid
     */
    public void setRegistrationTokenDuration(int registrationTokenDuration) {
        this.registrationTokenDuration = registrationTokenDuration;
    }

}
