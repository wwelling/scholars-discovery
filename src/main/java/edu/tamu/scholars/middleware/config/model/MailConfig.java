package edu.tamu.scholars.middleware.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Injectable middleware configuration to specify mail properties.
 * 
 * <p>See `middleware.mail` in src/main/resources/application.yml.</p>
 */
@Component
@ConfigurationProperties(prefix = "middleware.mail")
public class MailConfig {

    private String from = "scholarsdiscovery@gmail.com";

    private String replyTo = "scholarsdiscovery@gmail.com";

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

}
