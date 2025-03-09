package com.myhobbies.online.entertainmentservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("async")
public class AsyncProperties {

    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private String asyncThreadNamePrefix;
}
