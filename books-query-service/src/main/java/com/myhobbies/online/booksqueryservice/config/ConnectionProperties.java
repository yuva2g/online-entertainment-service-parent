package com.myhobbies.online.booksqueryservice.config;

public interface ConnectionProperties {
    int getConnectTimeoutMs();

    int getReadTimeoutMs();

    String getPath();

    String getUrl();

}
