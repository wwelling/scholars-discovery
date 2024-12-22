package edu.tamu.scholars.discovery.auth.config;

import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_REGISTRATION_TOKEN_DURATION;
import static edu.tamu.scholars.discovery.auth.AuthConstants.MIDDLEWARE_AUTH;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(MIDDLEWARE_AUTH)
public class AuthConfig {

    private PasswordConfig password = new PasswordConfig();

    private TokenConfig token = new TokenConfig();

    private int registrationTokenDuration = DEFAULT_REGISTRATION_TOKEN_DURATION;

    public AuthConfig() {
        // this configuration class is instantiated by reflection by Spring
    }

    public PasswordConfig getPassword() {
        return password;
    }

    public void setPassword(PasswordConfig password) {
        this.password = password;
    }

    public TokenConfig getToken() {
        return token;
    }

    public void setToken(TokenConfig token) {
        this.token = token;
    }

    public int getRegistrationTokenDuration() {
        return registrationTokenDuration;
    }

    public void setRegistrationTokenDuration(int registrationTokenDuration) {
        this.registrationTokenDuration = registrationTokenDuration;
    }

}
