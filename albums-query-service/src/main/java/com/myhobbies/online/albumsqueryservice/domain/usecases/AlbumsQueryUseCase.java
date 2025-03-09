package com.myhobbies.online.albumsqueryservice.domain.usecases;

import com.myhobbies.online.albumsqueryservice.domain.model.Album;
import com.myhobbies.online.albumsqueryservice.domain.port.inbound.AlbumsQueryService;
import com.myhobbies.online.albumsqueryservice.domain.port.outbound.AlbumsQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumsQueryUseCase implements AlbumsQueryService {

    private final AlbumsQueryPort albumsQueryPort;

    @Override
    public List<Album> getAlbum(String searchText, Integer limit) {
        return albumsQueryPort.getAlbums(searchText, limit);
    }
}
