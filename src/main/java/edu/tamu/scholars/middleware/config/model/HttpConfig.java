package edu.tamu.scholars.middleware.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Injectable middleware configuration to specify http properties.
 * 
 * <p>
 * See `middleware.http` in src/main/resources/application.yml.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "middleware.http")
public class HttpConfig {

    private int connectTimeout = 5;

    private int readTimeout = 30;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

}
