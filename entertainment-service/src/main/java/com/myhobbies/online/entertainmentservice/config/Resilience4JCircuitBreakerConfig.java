package com.myhobbies.online.entertainmentservice.config;

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
    public static final String ALBUMS_CIRCUIT_BREAKER = "AlbumsQueryService";
    public static final String BOOKS_CIRCUIT_BREAKER = "BooksQueryService";

    private final CircuitBreakerProperties circuitBreakerProperties;
    private final MeterRegistry meterRegistry;

    @Bean
    public CircuitBreaker albumsQueryCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return Optional.of(circuitBreakerRegistry.find(ALBUMS_CIRCUIT_BREAKER))
                .get().orElseThrow(() -> new IllegalArgumentException("Albums Query Service is unavailable"));
    }

    @Bean
    public CircuitBreaker booksQueryCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return Optional.of(circuitBreakerRegistry.find(BOOKS_CIRCUIT_BREAKER))
                .get().orElseThrow(() -> new IllegalArgumentException("Albums Query Service is unavailable"));
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig) {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        circuitBreakerRegistry.circuitBreaker(ALBUMS_CIRCUIT_BREAKER);
        circuitBreakerRegistry.circuitBreaker(BOOKS_CIRCUIT_BREAKER);
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
