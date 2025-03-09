package com.myhobbies.online.albumsqueryservice.adapter.itunesapi.mapper;

import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.model.ITunesAlbum;
import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.model.ITunesAlbumResults;
import com.myhobbies.online.albumsqueryservice.domain.model.Album;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ITunesAlbumMapper {

    public static List<Album> mapToAlbums(ITunesAlbumResults iTunesAlbumResults) {
        return iTunesAlbumResults.results().stream()
                .map(ITunesAlbumMapper::mapToAlbum)
                .toList();
    }
    private static Album mapToAlbum(ITunesAlbum iTunesAlbum) {
        return new Album(iTunesAlbum.collectionName(), iTunesAlbum.artistName());
    }
}
