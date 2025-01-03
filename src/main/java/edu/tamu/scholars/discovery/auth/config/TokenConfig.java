package edu.tamu.scholars.discovery.auth.config;

import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_TOKEN_PSEUDO_RANDOM_NUMBER_BYTES;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_TOKEN_SERVER_INTEGER;
import static edu.tamu.scholars.discovery.auth.AuthConstants.DEFAULT_TOKEN_SERVER_SECRET;
import static edu.tamu.scholars.discovery.auth.AuthConstants.MIDDLEWARE_AUTH_TOKEN;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(MIDDLEWARE_AUTH_TOKEN)
public class TokenConfig {

    // TODO: add validations and @Validated to the class

    private int serverInteger = DEFAULT_TOKEN_SERVER_INTEGER;

    private String serverSecret = DEFAULT_TOKEN_SERVER_SECRET;

    private int pseudoRandomNumberBytes = DEFAULT_TOKEN_PSEUDO_RANDOM_NUMBER_BYTES;

}
