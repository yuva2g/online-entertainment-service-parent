package com.myhobbies.online.booksqueryservice.client;

import com.myhobbies.online.booksqueryservice.client.models.Volumes;
import com.myhobbies.online.booksqueryservice.exception.GoogleBooksAPIServiceException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GoogleBooksAPIClientTest {

    private GoogleBooksAPIClient googleBooksAPIClient;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url(String.format("http://localhost:%s",
                        mockWebServer.getPort())).toString())
                .build();
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(java.time.Duration.ofMillis(1000))
                .build();
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("googleBooksCircuitBreaker");
        googleBooksAPIClient = new GoogleBooksAPIClient(webClient, circuitBreaker);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getVolumes_ValidRequest_ReturnsVolumes() {
        String jsonResponse = """
                {
                    "items": [
                        {
                            "volumeInfo": {
                                "title": "Test Title",
                                "authors": ["Test Author"]
                            }
                        }
                    ]
                }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        Volumes result = googleBooksAPIClient.getVolumes("Test", 5);

        assertEquals(1, result.items().size());
        assertEquals("Test Title", result.items().get(0).volumeInfo().title());
        assertEquals("Test Author", result.items().get(0).volumeInfo().authors().get(0));
    }

    @Test
    void getVolumes_ApiThrowsException_ThrowsGoogleBooksAPIServiceException() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        assertThrows(GoogleBooksAPIServiceException.class, () -> googleBooksAPIClient.getVolumes("Test", 5));
    }

    @Test
    void getVolumes_EmptyResponse_ReturnsEmptyVolumes() {
        String jsonResponse = """
                {
                    "items": []
                }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        Volumes result = googleBooksAPIClient.getVolumes("Test", 5);

        assertEquals(0, result.items().size());
    }
}