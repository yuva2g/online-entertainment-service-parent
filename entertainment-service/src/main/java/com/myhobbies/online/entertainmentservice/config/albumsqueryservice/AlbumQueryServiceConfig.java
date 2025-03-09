package com.myhobbies.online.entertainmentservice.config.albumsqueryservice;

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
public class AlbumQueryServiceConfig {

    private final AlbumsQueryServiceProperties albumsQueryServiceProperties;

    @Bean
    public HttpComponentsClientHttpRequestFactory albumsQueryHttpRequestFactory(MeterRegistry meterRegistry) {
        return HttpClientUtil.createHttpRequestFactory(albumsQueryServiceProperties, meterRegistry);
    }

    @Bean
    public RestTemplate albumsQueryRestTemplate(RestTemplateBuilder restTemplateBuilder,
                                                         HttpComponentsClientHttpRequestFactory albumsQueryHttpRequestFactory) {
        return DefaultRestTemplateBuilder.builder(restTemplateBuilder)
                .rootUri(albumsQueryServiceProperties.getUrl())
                .httpRequestFactory(albumsQueryHttpRequestFactory)
                .errorHandler(new ServiceResponseErrorHandler(ExternalService.ALBUMS_QUERY_SERVICE))
                .build();
    }
}
