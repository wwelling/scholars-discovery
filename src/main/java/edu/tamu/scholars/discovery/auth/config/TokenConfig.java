package edu.tamu.scholars.discovery.auth.config;

import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_TOKEN_PSEUDO_RANDOM_NUMBER_BYTES;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_TOKEN_SERVER_INTEGER;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_TOKEN_SERVER_SECRET;
import static edu.tamu.scholars.discovery.auth.AuthConstants.MIDDLEWARE_AUTH_TOKEN;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration class for authentication token settings in the Scholars
 * Discovery application. This class manages token generation parameters and
 * server-side security settings. It is automatically populated by Spring Boot's
 * configuration property binding mechanism using properties prefixed with
 * {@value MIDDLEWARE_AUTH_TOKEN}.
 * 
 * <p>
 * Default values are provided for all settings:
 * </p>
 * <ul>
 *   <li>Server Integer: {@value DEFAULT_TOKEN_SERVER_INTEGER}</li>
 *   <li>Server Secret: {@value DEFAULT_TOKEN_SERVER_SECRET}</li>
 *   <li>Pseudo Random Number Bytes: {@value DEFAULT_TOKEN_PSEUDO_RANDOM_NUMBER_BYTES}</li>
 * </ul>
 * 
 * <p>
 * These settings are used in combination to generate secure authentication
 * tokens with appropriate entropy and randomness for security purposes.
 * </p>
 *
 * @see edu.tamu.scholars.discovery.auth.config.AuthConfig
 * @since 1.5.0
 */
@Component
@ConfigurationProperties(MIDDLEWARE_AUTH_TOKEN)
public class TokenConfig {

    /**
     * Server-side integer value used in token generation. This value contributes to
     * the uniqueness of generated tokens.
     * Defaults to {@value DEFAULT_TOKEN_SERVER_INTEGER}.
     */
    private int serverInteger = DEFAULT_TOKEN_SERVER_INTEGER;

    /**
     * Server-side secret key used in token generation and validation. This secret
     * should be kept secure and not exposed outside the application.
     * Defaults to {@value DEFAULT_TOKEN_SERVER_SECRET}.
     */
    private String serverSecret = DEFAULT_TOKEN_SERVER_SECRET;

    /**
     * Number of bytes to use when generating pseudo-random numbers for tokens.
     * Higher values provide more entropy but require more computational resources.
     * Defaults to {@value DEFAULT_TOKEN_PSEUDO_RANDOM_NUMBER_BYTES} bytes.
     */
    private int pseudoRandomNumberBytes = DEFAULT_TOKEN_PSEUDO_RANDOM_NUMBER_BYTES;

    /**
     * Default constructor required for Spring's configuration property binding.
     * This constructor is called via reflection by Spring's configuration
     * mechanism.
     */
    public TokenConfig() {
        // this configuration class is instantiated by reflection by Spring
    }

    /**
     * Retrieves the server integer used in token generation.
     *
     * @return the server integer value
     */
    public int getServerInteger() {
        return serverInteger;
    }

    /**
     * Sets the server integer used in token generation.
     *
     * @param serverInteger the server integer value to set
     */
    public void setServerInteger(int serverInteger) {
        this.serverInteger = serverInteger;
    }

    /**
     * Retrieves the server secret used in token generation and validation.
     *
     * @return the server secret string
     */
    public String getServerSecret() {
        return serverSecret;
    }

    /**
     * Sets the server secret used in token generation and validation.
     *
     * @param serverSecret the server secret string to set
     */
    public void setServerSecret(String serverSecret) {
        this.serverSecret = serverSecret;
    }

    /**
     * Retrieves the number of bytes used for pseudo-random number generation.
     *
     * @return the number of bytes used for pseudo-random number generation
     */
    public int getPseudoRandomNumberBytes() {
        return pseudoRandomNumberBytes;
    }

    /**
     * Sets the number of bytes used for pseudo-random number generation.
     *
     * @param pseudoRandomNumberBytes the number of bytes to use for pseudo-random
     *                                number generation
     */
    public void setPseudoRandomNumberBytes(int pseudoRandomNumberBytes) {
        this.pseudoRandomNumberBytes = pseudoRandomNumberBytes;
    }

}
