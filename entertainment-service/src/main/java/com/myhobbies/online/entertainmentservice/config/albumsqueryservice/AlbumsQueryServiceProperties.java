package com.myhobbies.online.entertainmentservice.config.albumsqueryservice;

import com.myhobbies.online.entertainmentservice.config.ConnectionProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "albums-query-service")
@Getter
@Setter
public class AlbumsQueryServiceProperties implements ConnectionProperties {

    private int maxTotalConnections;
    private int maxConnectionsPerRoute;
    private int connectTimeoutMs;
    private int readTimeoutMs;
    private int requestTimeoutMs;
    private String url;
    private int defaultResultLimit;
}
