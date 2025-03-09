package com.myhobbies.online.albumsqueryservice.domain.port.outbound;

import com.myhobbies.online.albumsqueryservice.domain.model.Album;

import java.util.List;

public interface AlbumsQueryPort {
    List<Album> getAlbums(String searchText, Integer limit);
}
