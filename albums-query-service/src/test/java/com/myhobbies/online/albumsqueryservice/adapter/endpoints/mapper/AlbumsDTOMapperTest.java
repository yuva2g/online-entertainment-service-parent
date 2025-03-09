package com.myhobbies.online.albumsqueryservice.adapter.endpoints.mapper;

import com.myhobbies.online.albumsqueryservice.adapter.endpoints.dto.AlbumDTO;
import com.myhobbies.online.albumsqueryservice.domain.model.Album;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlbumsDTOMapperTest {

    private AlbumsDTOMapper albumsDTOMapper;

    @BeforeEach
    void setUp() {
        albumsDTOMapper = new AlbumsDTOMapper();
    }

    @Test
    void mapToDTOs_ValidAlbums_ReturnsAlbumDTOs() {
        Album album = new Album("Test Album", "Test Artist");
        List<Album> albums = Collections.singletonList(album);

        List<AlbumDTO> albumDTOs = albumsDTOMapper.mapToDTOs(albums);

        assertEquals(1, albumDTOs.size());
        assertEquals("Test Album", albumDTOs.getFirst().collectionName());
        assertEquals("Test Artist", albumDTOs.getFirst().artistName());
    }

    @Test
    void mapToDTOs_EmptyAlbums_ReturnsEmptyList() {
        List<Album> albums = Collections.emptyList();

        List<AlbumDTO> albumDTOs = albumsDTOMapper.mapToDTOs(albums);

        assertEquals(0, albumDTOs.size());
    }
}