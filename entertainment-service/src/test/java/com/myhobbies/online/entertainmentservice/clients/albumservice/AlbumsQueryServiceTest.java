package com.myhobbies.online.entertainmentservice.clients.albumservice;

import com.myhobbies.online.entertainmentservice.clients.albumservice.response.Album;
import com.myhobbies.online.entertainmentservice.models.Entertainment;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class AlbumsQueryServiceTest {

    private AlbumsQueryService albumsQueryService;
    private RestTemplate restTemplate;
    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        circuitBreaker = Mockito.mock(CircuitBreaker.class);
        albumsQueryService = new AlbumsQueryService(restTemplate, circuitBreaker);
    }

    @Test
    void getAlbums_ValidRequest_ReturnsAlbums() {
        Album album = new Album("Test Album", "Test Artist");
        List<Album> albums = List.of(album);
        ResponseEntity<List<Album>> responseEntity = ResponseEntity.ok(albums);

        Mockito.when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);
        Mockito.when(circuitBreaker.executeSupplier(any(Supplier.class))).thenAnswer(invocation -> ((Supplier<?>) invocation.getArgument(0)).get());

        List<Entertainment> result = albumsQueryService.getAlbums("Test");

        assertEquals(1, result.size());
        assertEquals("Test Album", result.getFirst().getTitle());
        assertEquals("Test Artist", result.getFirst().getAuthors().getFirst());
    }

    @Test
    void getAlbums_EmptyResponse_ReturnsEmptyList() {
        ResponseEntity<List<Album>> responseEntity = ResponseEntity.ok(Collections.emptyList());

        Mockito.when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);
        Mockito.when(circuitBreaker.executeSupplier(any(Supplier.class))).thenAnswer(invocation -> ((Supplier<?>) invocation.getArgument(0)).get());

        List<Entertainment> result = albumsQueryService.getAlbums("Test");

        assertEquals(0, result.size());
    }

    @Test
    void getAlbums_ApiThrowsException_FallbackWithEmptyResult() {
        Mockito.when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
                .thenThrow(new RuntimeException("Service unavailable"));
        Mockito.when(circuitBreaker.executeSupplier(any(Supplier.class))).thenAnswer(invocation -> ((Supplier<?>) invocation.getArgument(0)).get());

        List<Entertainment> result = albumsQueryService.getAlbums("Test");
        assertEquals(0, result.size());
    }
}