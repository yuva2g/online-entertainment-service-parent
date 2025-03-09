package com.myhobbies.online.albumsqueryservice.adapter.itunesapi.config;

import com.myhobbies.online.albumsqueryservice.config.ConnectionProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "itunes")
@Getter
@Setter
public class ITunesProperties implements ConnectionProperties {

    private int maxTotalConnections;
    private int maxConnectionsPerRoute;
    private int connectTimeoutMs;
    private int readTimeoutMs;
    private int requestTimeoutMs;
    private String url;
    private int defaultResultLimit;
}
