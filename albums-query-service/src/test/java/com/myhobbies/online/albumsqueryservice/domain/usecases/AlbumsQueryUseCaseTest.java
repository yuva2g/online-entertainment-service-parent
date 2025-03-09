package com.myhobbies.online.albumsqueryservice.domain.usecases;

import com.myhobbies.online.albumsqueryservice.domain.model.Album;
import com.myhobbies.online.albumsqueryservice.domain.port.outbound.AlbumsQueryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

class AlbumsQueryUseCaseTest {

    private AlbumsQueryUseCase albumsQueryUseCase;
    private AlbumsQueryPort albumsQueryPort;

    @BeforeEach
    void setUp() {
        albumsQueryPort = Mockito.mock(AlbumsQueryPort.class);
        albumsQueryUseCase = new AlbumsQueryUseCase(albumsQueryPort);
    }

    @Test
    void getAlbum_ValidRequest_ReturnsAlbums() {
        Album album = new Album("Test Album", "Test Artist");
        List<Album> albums = Collections.singletonList(album);

        Mockito.when(albumsQueryPort.getAlbums(anyString(), anyInt())).thenReturn(albums);

        List<Album> result = albumsQueryUseCase.getAlbum("Test", 5);

        assertEquals(1, result.size());
        assertEquals("Test Album", result.get(0).collectionName());
        assertEquals("Test Artist", result.get(0).artistName());
    }

    @Test
    void getAlbum_EmptyResult_ReturnsEmptyList() {
        Mockito.when(albumsQueryPort.getAlbums(anyString(), anyInt())).thenReturn(Collections.emptyList());

        List<Album> result = albumsQueryUseCase.getAlbum("Test", 5);

        assertEquals(0, result.size());
    }
}