package edu.tamu.scholars.discovery.auth.config;

import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_DURATION;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_MAX_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_PASSWORD_MIN_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.MIDDLEWARE_AUTH_PASSWORD;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(MIDDLEWARE_AUTH_PASSWORD)
public class PasswordConfig {

    // TODO: add validations and @Validated to the class

    private int duration = DEFAULT_PASSWORD_DURATION;

    private int minLength = DEFAULT_PASSWORD_MIN_LENGTH;

    private int maxLength = DEFAULT_PASSWORD_MAX_LENGTH;

}
