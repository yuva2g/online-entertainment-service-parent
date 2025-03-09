package com.myhobbies.online.entertainmentservice.config.httpclient;

import com.myhobbies.online.entertainmentservice.config.ConnectionProperties;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@UtilityClass
@Slf4j
public class HttpClientUtil {

    public static HttpComponentsClientHttpRequestFactory createHttpRequestFactory(ConnectionProperties properties,
                                                                                  MeterRegistry meterRegistry) {
        final CloseableHttpClient httpClient = buildHttpClient(properties, meterRegistry);
        return createAndConfigureHttpRequestFactory(httpClient, properties);
    }

    private static CloseableHttpClient buildHttpClient(ConnectionProperties properties,
                                                       MeterRegistry meterRegistry) {
        final SocketConfig.Builder socketConfigBuilder = SocketConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(properties.getReadTimeoutMs()));
        final HttpClientBuilder clientBuilder = HttpClients.custom()
                .setConnectionManagerShared(true)
                .disableConnectionState();

        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultSocketConfig(socketConfigBuilder.build());
        connectionManager.setMaxTotal(properties.getMaxTotalConnections());
        connectionManager.setDefaultMaxPerRoute(properties.getMaxConnectionsPerRoute());

        return clientBuilder
                .setConnectionManager(connectionManager)
                .build();
    }

    private static HttpComponentsClientHttpRequestFactory createAndConfigureHttpRequestFactory(CloseableHttpClient client, ConnectionProperties properties) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        factory.setConnectionRequestTimeout(properties.getRequestTimeoutMs());
        factory.setConnectTimeout(properties.getConnectTimeoutMs());
        return factory;
    }
}
