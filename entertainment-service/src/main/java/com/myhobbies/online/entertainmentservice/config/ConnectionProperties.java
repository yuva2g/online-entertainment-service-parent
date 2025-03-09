package com.myhobbies.online.entertainmentservice.config;

public interface ConnectionProperties {
    int getConnectTimeoutMs();

    int getReadTimeoutMs();

    int getRequestTimeoutMs();

    int getMaxTotalConnections();

    int getMaxConnectionsPerRoute();

    String getUrl();

}
