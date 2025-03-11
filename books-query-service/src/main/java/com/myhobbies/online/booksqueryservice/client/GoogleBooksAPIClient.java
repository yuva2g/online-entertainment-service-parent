package com.myhobbies.online.booksqueryservice.client;

import com.myhobbies.online.booksqueryservice.client.models.Volumes;
import com.myhobbies.online.booksqueryservice.config.googlebooksapi.GoogleBooksConnectionProperties;
import com.myhobbies.online.booksqueryservice.exception.GoogleBooksAPIServiceException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GoogleBooksAPIClient {

    private final WebClient googleBooksWebClient;

    private final CircuitBreaker googleBooksCircuitBreaker;

    private final GoogleBooksConnectionProperties googleBooksConnectionProperties;

    @Timed(value = "http.google-books-api.request", extraTags = {"backend", "google-books-api", "operation", "getVolumes"})
    public Volumes getVolumes(String searchText, int limit) {
        return googleBooksWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes")
                        .queryParam("q", searchText)
                        .queryParam("maxResults", limit)
                        .queryParam("key", googleBooksConnectionProperties.getApiKey())
                        .build())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        this::handleClientErrorResponse
                )
                .bodyToMono(Volumes.class)
                .transform(CircuitBreakerOperator.of(googleBooksCircuitBreaker))
                .onErrorResume(throwable -> {
                    throw new GoogleBooksAPIServiceException("Google Books API is currently unavailable", throwable);
                })
                .block();
    }

    private Mono<Throwable> handleClientErrorResponse(ClientResponse clientResponse) {
        return switch (clientResponse.statusCode().value()) {
            case 404 -> Mono.error(new GoogleBooksAPIServiceException("Resource not found"));
            case 401 -> Mono.error(new GoogleBooksAPIServiceException("Unauthorized"));
            case 500 -> Mono.error(new GoogleBooksAPIServiceException("Internal server error"));
            default ->
                    Mono.error(new GoogleBooksAPIServiceException("Internal server error with status code: "
                            + clientResponse.statusCode().value()));
        };
    }
}
