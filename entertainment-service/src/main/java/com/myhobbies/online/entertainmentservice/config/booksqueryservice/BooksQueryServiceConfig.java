package com.myhobbies.online.entertainmentservice.config.booksqueryservice;

import com.myhobbies.online.entertainmentservice.config.ExternalService;
import com.myhobbies.online.entertainmentservice.config.httpclient.DefaultRestTemplateBuilder;
import com.myhobbies.online.entertainmentservice.config.httpclient.HttpClientUtil;
import com.myhobbies.online.entertainmentservice.exception.ServiceResponseErrorHandler;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class BooksQueryServiceConfig {

    private final BooksQueryServiceProperties booksQueryServiceProperties;

    @Bean
    public HttpComponentsClientHttpRequestFactory booksQueryHttpRequestFactory(MeterRegistry meterRegistry) {
        return HttpClientUtil.createHttpRequestFactory(booksQueryServiceProperties, meterRegistry);
    }

    @Bean
    public RestTemplate booksQueryRestTemplate(RestTemplateBuilder restTemplateBuilder,
                                                         HttpComponentsClientHttpRequestFactory albumsQueryHttpRequestFactory) {
        return DefaultRestTemplateBuilder.builder(restTemplateBuilder)
                .rootUri(booksQueryServiceProperties.getUrl())
                .httpRequestFactory(albumsQueryHttpRequestFactory)
                .errorHandler(new ServiceResponseErrorHandler(ExternalService.BOOKS_QUERY_SERVICE))
                .build();
    }
}
