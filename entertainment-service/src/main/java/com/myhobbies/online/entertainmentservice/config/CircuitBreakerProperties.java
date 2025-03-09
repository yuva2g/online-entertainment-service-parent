package com.myhobbies.online.entertainmentservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "circuit-breaker")
public class CircuitBreakerProperties {

    private int failureRateThreshold;
    private int waitDurationInOpenState;
    private int permittedNumberOfCallsInHalfOpenState;
    private int slidingWindowSize;
    private int minimumNumberOfCalls;
}
