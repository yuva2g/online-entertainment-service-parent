package com.myhobbies.online.albumsqueryservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Optional;

@EnableConfigurationProperties(CircuitBreakerProperties.class)
@Configuration
@RequiredArgsConstructor
public class Resilience4JCircuitBreakerConfig {
    public static final String ITUNES_CIRCUIT_BREAKER = "ITunesAPIAdapter";

    private final CircuitBreakerProperties circuitBreakerProperties;
    private final MeterRegistry meterRegistry;

    @Bean
    public CircuitBreaker iTunesCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return Optional.of(circuitBreakerRegistry.find(ITUNES_CIRCUIT_BREAKER))
                .get().orElseThrow(() -> new IllegalArgumentException("iTunes API is unavailable"));
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig) {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        circuitBreakerRegistry.circuitBreaker(ITUNES_CIRCUIT_BREAKER);
        TaggedCircuitBreakerMetrics
                .ofCircuitBreakerRegistry(circuitBreakerRegistry)
                .bindTo(meterRegistry);
        return circuitBreakerRegistry;
    }

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(circuitBreakerProperties.getFailureRateThreshold())
                .waitDurationInOpenState(Duration.ofMillis(circuitBreakerProperties.getWaitDurationInOpenState()))
                .permittedNumberOfCallsInHalfOpenState(circuitBreakerProperties.getPermittedNumberOfCallsInHalfOpenState())
                .slidingWindowSize(circuitBreakerProperties.getSlidingWindowSize())
                .minimumNumberOfCalls(circuitBreakerProperties.getMinimumNumberOfCalls())
                .build();
    }
}
