package edu.tamu.scholars.discovery.auth.config;

import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_REGISTRATION_TOKEN_DURATION;
import static edu.tamu.scholars.discovery.auth.AuthConstants.MIDDLEWARE_AUTH;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(MIDDLEWARE_AUTH)
public class AuthConfig {

    // TODO: add validations and @Validated to the class

    private PasswordConfig password = new PasswordConfig();

    private TokenConfig token = new TokenConfig();

    private int registrationTokenDuration = DEFAULT_REGISTRATION_TOKEN_DURATION;

}
