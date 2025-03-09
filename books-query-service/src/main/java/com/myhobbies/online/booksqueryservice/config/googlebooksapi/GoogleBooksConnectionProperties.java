package com.myhobbies.online.booksqueryservice.config.googlebooksapi;

import com.myhobbies.online.booksqueryservice.config.ConnectionProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "google.books")
public class GoogleBooksConnectionProperties implements ConnectionProperties {

    private int connectTimeoutMs;
    private int readTimeoutMs;
    private String url;
    private String path;
    private int defaultResultLimit;
    private String clientRegistrationId;
    private String apiKey;
}
