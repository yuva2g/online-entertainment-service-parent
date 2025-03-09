package com.myhobbies.online.albumsqueryservice.adapter.endpoints;

import com.myhobbies.online.albumsqueryservice.adapter.endpoints.dto.AlbumDTO;
import com.myhobbies.online.albumsqueryservice.adapter.endpoints.mapper.AlbumsDTOMapper;
import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.config.ITunesProperties;
import com.myhobbies.online.albumsqueryservice.domain.model.Album;
import com.myhobbies.online.albumsqueryservice.domain.usecases.AlbumsQueryUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlbumsQueryController.class)
class AlbumsQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlbumsQueryUseCase albumsQueryService;

    @MockitoBean
    private AlbumsDTOMapper albumsDTOMapper;

    @MockitoBean
    private ITunesProperties iTunesProperties;

    @Test
    void getAlbums_ValidRequest_ReturnsAlbums() throws Exception {
        Album album = new Album("Test Album", "Test Artist");
        AlbumDTO albumDTO = new AlbumDTO("Test Album", "Test Artist");
        List<Album> albums = Collections.singletonList(album);
        List<AlbumDTO> albumDTOs = Collections.singletonList(albumDTO);

        Mockito.when(albumsQueryService.getAlbum(anyString(), anyInt())).thenReturn(albums);
        Mockito.when(albumsDTOMapper.mapToDTOs(albums)).thenReturn(albumDTOs);
        Mockito.when(iTunesProperties.getDefaultResultLimit()).thenReturn(10);

        mockMvc.perform(MockMvcRequestBuilders.get("/albums")
                        .param("searchText", "Test")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'collectionName':'Test Album','artistName':'Test Artist'}]"));
    }

    @Test
    void getAlbums_ValidRequest_ReturnsEmptyAlbums() throws Exception {
        List<Album> albums = Collections.emptyList();
        List<AlbumDTO> albumDTOs = Collections.emptyList();

        Mockito.when(albumsQueryService.getAlbum(anyString(), anyInt())).thenReturn(albums);
        Mockito.when(albumsDTOMapper.mapToDTOs(albums)).thenReturn(albumDTOs);
        Mockito.when(iTunesProperties.getDefaultResultLimit()).thenReturn(10);

        mockMvc.perform(MockMvcRequestBuilders.get("/albums")
                        .param("searchText", "Test")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAlbums_InvalidSearchText_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/albums")
                        .param("searchText", "Invalid@Text")
                        .param("limit", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAlbums_NegativeLimit_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/albums")
                        .param("searchText", "Test")
                        .param("limit", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAlbums_MissingSearchText_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/albums")
                        .param("limit", "5"))
                .andExpect(status().isBadRequest());
    }
}
