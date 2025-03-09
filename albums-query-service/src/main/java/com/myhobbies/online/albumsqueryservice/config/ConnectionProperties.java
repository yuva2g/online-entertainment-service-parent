package com.myhobbies.online.albumsqueryservice.config;

public interface ConnectionProperties {
    int getConnectTimeoutMs();

    int getReadTimeoutMs();

    int getRequestTimeoutMs();

    int getMaxTotalConnections();

    int getMaxConnectionsPerRoute();

    String getUrl();

}
