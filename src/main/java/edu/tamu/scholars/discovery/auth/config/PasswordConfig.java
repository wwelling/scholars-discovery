package edu.tamu.scholars.discovery.auth.config;

import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_DURATION;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_MAX_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_MIN_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.MIDDLEWARE_AUTH_PASSWORD;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration class for password-related settings in the Scholars Discovery
 * application. This class manages password policies including duration and
 * length requirements. It is automatically populated by Spring Boot's
 * configuration property binding mechanism using properties prefixed with
 * {@value MIDDLEWARE_AUTH_PASSWORD}.
 * 
 * <p>
 * Default values are provided for all settings:
 * </p>
 * <ul>
 *   <li>Password Duration: {@value DEFAULT_PASSWORD_DURATION} days</li>
 *   <li>Minimum Length: {@value DEFAULT_PASSWORD_MIN_LENGTH} characters</li>
 *   <li>Maximum Length: {@value DEFAULT_PASSWORD_MAX_LENGTH} characters</li>
 * </ul>
 *
 * @see edu.tamu.scholars.discovery.auth.config.AuthConfig
 * @since 1.5.0
 */
@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(MIDDLEWARE_AUTH_PASSWORD)
public class PasswordConfig {

    // TODO: add validations and @Validated to the class

    /**
     * The duration (in days) for which a password remains valid before requiring
     * change.
     * Defaults to {@value DEFAULT_PASSWORD_DURATION} days.
     */
    private int duration = DEFAULT_PASSWORD_DURATION;

    /**
     * The minimum required length for passwords.
     * Defaults to {@value DEFAULT_PASSWORD_MIN_LENGTH} characters.
     */
    private int minLength = DEFAULT_PASSWORD_MIN_LENGTH;

    /**
     * The maximum allowed length for passwords.
     * Defaults to {@value DEFAULT_PASSWORD_MAX_LENGTH} characters.
     */
    private int maxLength = DEFAULT_PASSWORD_MAX_LENGTH;

}
