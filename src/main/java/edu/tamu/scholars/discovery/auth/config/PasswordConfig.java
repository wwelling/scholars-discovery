package edu.tamu.scholars.discovery.auth.config;

import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_DURATION;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_MAX_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_MIN_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.MIDDLEWARE_AUTH_PASSWORD;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(MIDDLEWARE_AUTH_PASSWORD)
public class PasswordConfig {

    private int duration = DEFAULT_PASSWORD_DURATION;

    private int minLength = DEFAULT_PASSWORD_MIN_LENGTH;

    private int maxLength = DEFAULT_PASSWORD_MAX_LENGTH;

    public PasswordConfig() {
        // this configuration class is instantiated by reflection by Spring
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

}
