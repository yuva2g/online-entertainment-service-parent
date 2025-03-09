package com.myhobbies.online.albumsqueryservice.adapter.itunesapi.config;

import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.config.exception.ITunesResponseErrorHandler;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.httpcomponents.hc5.PoolingHttpClientConnectionManagerMetricsBinder;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@EnableConfigurationProperties(ITunesProperties.class)
@RequiredArgsConstructor
@Configuration
public class ITunesAPIConfig {

    private final ITunesProperties iTunesProperties;

    @Bean
    public RestTemplate iTunesAPIRestTemplate(final RestTemplateBuilder restTemplateBuilder,
                                           final HttpComponentsClientHttpRequestFactory iTunesHttpRequestFactory) {
        return restTemplateBuilder
                .rootUri(iTunesProperties.getUrl())
                .requestFactory(() -> new BufferingClientHttpRequestFactory(iTunesHttpRequestFactory))
                .messageConverters(List.of(new TextJavascriptMessageConverter()))
                .errorHandler(new ITunesResponseErrorHandler())
                .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory iTunesHttpRequestFactory(MeterRegistry meterRegistry) {
        final CloseableHttpClient httpClient = buildHttpClient(meterRegistry);
        return createAndConfigureHttpRequestFactory(httpClient);
    }

    private CloseableHttpClient buildHttpClient(MeterRegistry meterRegistry) {
        final SocketConfig.Builder socketConfigBuilder = SocketConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(iTunesProperties.getReadTimeoutMs()));
        final HttpClientBuilder clientBuilder = HttpClients.custom()
                .setConnectionManagerShared(true)
                .disableConnectionState();


        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultSocketConfig(socketConfigBuilder.build());
        connectionManager.setMaxTotal(iTunesProperties.getMaxTotalConnections());
        connectionManager.setDefaultMaxPerRoute(iTunesProperties.getMaxConnectionsPerRoute());

        new PoolingHttpClientConnectionManagerMetricsBinder(connectionManager, "ITUNES")
                .bindTo(meterRegistry);

        return clientBuilder
                .setConnectionManager(connectionManager)
                .build();
    }

    private HttpComponentsClientHttpRequestFactory createAndConfigureHttpRequestFactory(CloseableHttpClient client) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        factory.setConnectionRequestTimeout(iTunesProperties.getRequestTimeoutMs());
        factory.setConnectTimeout(iTunesProperties.getConnectTimeoutMs());
        return factory;
    }
}
