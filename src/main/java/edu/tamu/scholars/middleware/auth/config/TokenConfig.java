package edu.tamu.scholars.middleware.auth.config;

import static edu.tamu.scholars.middleware.auth.AuthConstants.DEFAULT_TOKEN_PSEUDO_RANDOM_NUMBER_BYTES;
import static edu.tamu.scholars.middleware.auth.AuthConstants.DEFAULT_TOKEN_SERVER_INTEGER;
import static edu.tamu.scholars.middleware.auth.AuthConstants.DEFAULT_TOKEN_SERVER_SECRET;
import static edu.tamu.scholars.middleware.auth.AuthConstants.MIDDLEWARE_AUTH_TOKEN;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(MIDDLEWARE_AUTH_TOKEN)
public class TokenConfig {

    private int serverInteger = DEFAULT_TOKEN_SERVER_INTEGER;

    private String serverSecret = DEFAULT_TOKEN_SERVER_SECRET;

    private int pseudoRandomNumberBytes = DEFAULT_TOKEN_PSEUDO_RANDOM_NUMBER_BYTES;

    public TokenConfig() {
        // this configuration class is instantiated by reflection by Spring
    }

    public int getServerInteger() {
        return serverInteger;
    }

    public void setServerInteger(int serverInteger) {
        this.serverInteger = serverInteger;
    }

    public String getServerSecret() {
        return serverSecret;
    }

    public void setServerSecret(String serverSecret) {
        this.serverSecret = serverSecret;
    }

    public int getPseudoRandomNumberBytes() {
        return pseudoRandomNumberBytes;
    }

    public void setPseudoRandomNumberBytes(int pseudoRandomNumberBytes) {
        this.pseudoRandomNumberBytes = pseudoRandomNumberBytes;
    }

}
