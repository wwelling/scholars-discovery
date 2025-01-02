package edu.tamu.scholars.discovery.factory.rest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import io.micrometer.observation.ObservationRegistry;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.observation.ClientRequestObservationConvention;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

public class ManagedRestTemplate extends RestTemplate {

    private final PoolingHttpClientConnectionManager connectionManager;

    private final CloseableHttpClient httpClient;

    private final HttpComponentsClientHttpRequestFactory requestFactory;

    private ManagedRestTemplate(
            PoolingHttpClientConnectionManager connectionManager,
            CloseableHttpClient httpClient,
            HttpComponentsClientHttpRequestFactory requestFactory) {
        super();
        this.connectionManager = connectionManager;
        this.httpClient = httpClient;
        this.requestFactory = requestFactory;
        this.withRequestFactory(requestFactory);
    }

    public ManagedRestTemplate withErrorHandler(ResponseErrorHandler errorHandler) {
        super.setErrorHandler(errorHandler);

        return this;
    }

    public ManagedRestTemplate withClientHttpRequestInitializers(List<ClientHttpRequestInitializer> initializers) {
        super.setClientHttpRequestInitializers(initializers);

        return this;
    }

    public ManagedRestTemplate withDefaultUriVariables(Map<String, ?> uriVars) {
        super.setDefaultUriVariables(uriVars);

        return this;
    }

    public ManagedRestTemplate withInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        super.setInterceptors(interceptors);

        return this;
    }

    public ManagedRestTemplate withMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        super.setMessageConverters(messageConverters);

        return this;
    }

    public ManagedRestTemplate withObservationConvention(ClientRequestObservationConvention observationConvention) {
        super.setObservationConvention(observationConvention);

        return this;
    }

    public ManagedRestTemplate withObservationRegistry(ObservationRegistry registry) {
        super.setObservationRegistry(registry);

        return this;
    }

    public ManagedRestTemplate withRequestFactory(ClientHttpRequestFactory requestFactory) {
        super.setRequestFactory(requestFactory);

        return this;
    }

    public ManagedRestTemplate withUriTemplateHandler(UriTemplateHandler handler) {
        super.setUriTemplateHandler(handler);

        return this;
    }

    @Override
    public void setErrorHandler(ResponseErrorHandler errorHandler) {
        throw new UnsupportedOperationException(
            "setErrorHandler not supported. Please use withErrorHandler.");
    }

    @Override
    public void setClientHttpRequestInitializers(List<ClientHttpRequestInitializer> initializers) {
        throw new UnsupportedOperationException(
            "setClientHttpRequestInitializers not supported. Please use withClientHttpRequestInitializers.");
    }

    @Override
    public void setDefaultUriVariables(Map<String, ?> uriVars) {
        throw new UnsupportedOperationException(
            "setDefaultUriVariables not supported. Please use withDefaultUriVariables.");
    }

    @Override
    public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        throw new UnsupportedOperationException(
            "setInterceptors not supported. Please use withInterceptors.");
    }

    @Override
    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        throw new UnsupportedOperationException(
            "setMessageConverters not supported. Please use withMessageConverters.");
    }

    @Override
    public void setObservationConvention(ClientRequestObservationConvention observationConvention) {
        throw new UnsupportedOperationException(
            "setObservationConvention not supported. Please use withObservationConvention.");
    }

    @Override
    public void setObservationRegistry(ObservationRegistry registry) {
        throw new UnsupportedOperationException(
            "setObservationRegistry not supported. Please use withObservationRegistry.");
    }

    @Override
    public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
        throw new UnsupportedOperationException(
            "setRequestFactory not supported. Please use withRequestFactory.");
    }

    @Override
    public void setUriTemplateHandler(UriTemplateHandler handler) {
        throw new UnsupportedOperationException(
            "setUriTemplateHandler not supported. Please use withUriTemplateHandler.");
    }

    public void destroy() {
        try {
            this.connectionManager.close();
            this.httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.requestFactory.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static ManagedRestTemplate with(ManagedRestConfig config) {
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setTimeToLive(TimeValue.ofMinutes(config.getTimeToLive()))
            .build();

        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setMaxConnTotal(config.getMaxConnTotal())
            .setMaxConnPerRoute(config.getMaxConnPerRoute())
            .setDefaultConnectionConfig(connectionConfig)
            .build();

        CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
            .evictIdleConnections(TimeValue.ofMinutes(config.getEvictIdleConnections()))
            .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout((int) Duration.ofSeconds(config.getConnectTimeout()).toMillis());
        requestFactory.setReadTimeout((int) Duration.ofSeconds(config.getReadTimeout()).toMillis());

        return new ManagedRestTemplate(connectionManager, httpClient, requestFactory);
    }

}
