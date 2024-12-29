package edu.tamu.scholars.discovery.config.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "discovery.mail")
public class MailConfig {

    // TODO: add validations and @Validated to the class

    private String from = "scholarsdiscovery@gmail.com";

    private String replyTo = "scholarsdiscovery@gmail.com";

}
