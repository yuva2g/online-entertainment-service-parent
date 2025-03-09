package com.myhobbies.online.booksqueryservice.config.googlebooksapi;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GoogleBooksConnectionProperties.class)
public class GoogleBooksAPIConfig {

    private final GoogleBooksConnectionProperties googleBooksConnectionProperties;

    @Bean
    public WebClient googleBooksWebClient(ExchangeFilterFunction exchangeFilterFunction) {
        HttpClient httpClient = createHttpClient();
        return buildWebClient(httpClient, exchangeFilterFunction);
    }

    private WebClient buildWebClient(HttpClient httpClient, ExchangeFilterFunction oAuthClientCredentialsWebClientFilter) {
        return WebClient.builder()
                .uriBuilderFactory(new DefaultUriBuilderFactory(googleBooksConnectionProperties.getUrl()))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(oAuthClientCredentialsWebClientFilter)
                .build();
    }

    private HttpClient createHttpClient() {
        return HttpClient.create()
                .baseUrl(googleBooksConnectionProperties.getUrl())
                .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(googleBooksConnectionProperties.getReadTimeoutMs())))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, googleBooksConnectionProperties.getConnectTimeoutMs());
    }
}
