package com.myhobbies.online.albumsqueryservice.adapter.itunesapi;

import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.config.exception.ITunesAPIException;
import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.model.ITunesAlbum;
import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.model.ITunesAlbumResults;
import com.myhobbies.online.albumsqueryservice.domain.model.Album;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class ITunesAlbumsAdapterTest {

    private ITunesAlbumsAdapter iTunesAlbumsAdapter;
    private RestTemplate restTemplate;
    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        circuitBreaker = Mockito.mock(CircuitBreaker.class);
        iTunesAlbumsAdapter = new ITunesAlbumsAdapter(restTemplate, circuitBreaker);
    }

    @Test
    void getAlbums_ValidRequest_ReturnsAlbums() {
        ITunesAlbumResults results = new ITunesAlbumResults(1, Collections.singletonList(new ITunesAlbum("Test Album", "Test Artist")));
        Mockito.when(restTemplate.getForObject(anyString(), eq(ITunesAlbumResults.class), anyString(), anyInt())).thenReturn(results);
        Mockito.when(circuitBreaker.executeSupplier(any(Supplier.class))).thenAnswer(invocation -> ((Supplier<?>) invocation.getArgument(0)).get());

        List<Album> albums = iTunesAlbumsAdapter.getAlbums("Test", 5);

        assertEquals(1, albums.size());
        assertEquals("Test Album", albums.getFirst().collectionName());
        assertEquals("Test Artist", albums.getFirst().artistName());
    }

    @Test
    void getAlbums_APIThrowsException_ThrowsITunesAPIException() {
        Mockito.when(restTemplate.getForObject(anyString(), eq(ITunesAlbumResults.class), anyString(), anyInt())).thenThrow(new RuntimeException("API error"));
        Mockito.when(circuitBreaker.executeSupplier(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        assertThrows(ITunesAPIException.class, () -> iTunesAlbumsAdapter.getAlbums("Test", 5));
    }

    @Test
    void getAlbums_CircuitBreakerOpen_ThrowsITunesAPIException() {
        Mockito.when(circuitBreaker.executeSupplier(any(Supplier.class))).thenThrow(new ITunesAPIException("Circuit breaker open"));

        assertThrows(ITunesAPIException.class, () -> iTunesAlbumsAdapter.getAlbums("Test", 5));
    }
}