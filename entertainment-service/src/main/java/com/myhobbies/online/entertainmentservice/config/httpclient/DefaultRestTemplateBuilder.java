package com.myhobbies.online.entertainmentservice.config.httpclient;


import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Accessors(fluent = true)
public final class DefaultRestTemplateBuilder {
    private RestTemplateBuilder builder;

    @Setter
    private HttpComponentsClientHttpRequestFactory httpRequestFactory;

    @Setter
    private List<ClientHttpRequestInterceptor> additionalInterceptors;

    @Setter
    private List<HttpMessageConverter<?>> httpMessageConverters;

    @Setter
    private String rootUri;

    @Setter
    private ResponseErrorHandler errorHandler;

    private DefaultRestTemplateBuilder(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    public static DefaultRestTemplateBuilder builder(RestTemplateBuilder builder) {
        return new DefaultRestTemplateBuilder(builder);
    }

    public RestTemplate build() {

        if (httpRequestFactory == null) {
            throw new IllegalArgumentException("httpRequestFactory cannot be null");
        }

        if (rootUri == null) {
            throw new IllegalArgumentException("rootUri cannot be null");
        }

        builder = builder.rootUri(rootUri).requestFactory(() -> new BufferingClientHttpRequestFactory(httpRequestFactory));


        if (additionalInterceptors != null) {
            builder = builder.additionalInterceptors(additionalInterceptors);
        }

        if (httpMessageConverters != null) {
            builder = builder.messageConverters(httpMessageConverters);
        }

        if (errorHandler != null) {
            builder = builder.errorHandler(errorHandler);
        }

        return builder.build();
    }
}

