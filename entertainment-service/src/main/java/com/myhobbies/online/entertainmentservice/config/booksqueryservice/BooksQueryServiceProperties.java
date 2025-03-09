package com.myhobbies.online.entertainmentservice.config.booksqueryservice;

import com.myhobbies.online.entertainmentservice.config.ConnectionProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "books-query-service")
@Getter
@Setter
public class BooksQueryServiceProperties implements ConnectionProperties {

    private int maxTotalConnections;
    private int maxConnectionsPerRoute;
    private int connectTimeoutMs;
    private int readTimeoutMs;
    private int requestTimeoutMs;
    private String url;
    private int defaultResultLimit;
}
