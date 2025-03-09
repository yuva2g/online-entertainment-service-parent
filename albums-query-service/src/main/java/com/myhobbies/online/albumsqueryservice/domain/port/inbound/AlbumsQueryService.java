package com.myhobbies.online.albumsqueryservice.domain.port.inbound;

import com.myhobbies.online.albumsqueryservice.domain.model.Album;

import java.util.List;

public interface AlbumsQueryService {

    List<Album> getAlbum(String searchText, Integer limit);
}
