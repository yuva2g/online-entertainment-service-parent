package com.myhobbies.online.albumsqueryservice.adapter.endpoints.mapper;

import com.myhobbies.online.albumsqueryservice.adapter.endpoints.dto.AlbumDTO;
import com.myhobbies.online.albumsqueryservice.domain.model.Album;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlbumsDTOMapper {

    private AlbumDTO mapToAlbumDTO(Album album) {
        return new AlbumDTO(album.collectionName(), album.artistName());
    }

    public List<AlbumDTO> mapToDTOs(List<Album> albums) {
        return albums.stream().map(this::mapToAlbumDTO).toList();
    }
}
