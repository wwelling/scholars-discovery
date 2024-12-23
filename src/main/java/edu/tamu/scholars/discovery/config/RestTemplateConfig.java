package edu.tamu.scholars.discovery.config;

import java.time.Duration;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import edu.tamu.scholars.discovery.config.model.HttpConfig;

@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder, HttpConfig config) {
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setMaxConnTotal(config.getMaxTotalConnections())
            .setMaxConnPerRoute(config.getMaxConnectionsPerRoute())
            .setDefaultConnectionConfig(ConnectionConfig.custom()
                .setTimeToLive(TimeValue.ofMinutes(config.getTimeToLive()))
                .build())
            .build();

        CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
            .evictIdleConnections(TimeValue.ofMinutes(config.getEvictIdleConnections()))
            .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return builder
            .requestFactory(() -> requestFactory)
            .connectTimeout(Duration.ofSeconds(config.getConnectTimeout()))
            .readTimeout(Duration.ofSeconds(config.getReadTimeout()))
            .errorHandler(new DefaultResponseErrorHandler())
            .build();
    }

}
