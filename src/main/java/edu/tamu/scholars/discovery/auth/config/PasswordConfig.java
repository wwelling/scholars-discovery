package edu.tamu.scholars.discovery.auth.config;

import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_DURATION;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_MAX_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_MIN_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.MIDDLEWARE_AUTH_PASSWORD;

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
@Component
@ConfigurationProperties(MIDDLEWARE_AUTH_PASSWORD)
public class PasswordConfig {

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

    /**
     * Default constructor required for Spring's configuration property binding.
     * This constructor is called via reflection by Spring's configuration
     * mechanism.
     */
    public PasswordConfig() {
        // this configuration class is instantiated by reflection by Spring
    }

    /**
     * Retrieves the password validity duration.
     *
     * @return the number of days a password remains valid before requiring change
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the password validity duration.
     *
     * @param duration the number of days a password should remain valid
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Retrieves the minimum required password length.
     *
     * @return the minimum number of characters required for passwords
     */
    public int getMinLength() {
        return minLength;
    }

    /**
     * Sets the minimum required password length.
     *
     * @param minLength the minimum number of characters required for passwords
     */
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    /**
     * Retrieves the maximum allowed password length.
     *
     * @return the maximum number of characters allowed for passwords
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the maximum allowed password length.
     *
     * @param maxLength the maximum number of characters allowed for passwords
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

}
