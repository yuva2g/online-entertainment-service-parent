package com.myhobbies.online.entertainmentservice.clients.albumservice.response;

import com.myhobbies.online.entertainmentservice.models.Entertainment;
import com.myhobbies.online.entertainmentservice.models.EntertainmentType;

import java.util.List;

public class AlbumsResponseTranslator {

    public static List<Entertainment> toEntertainments(List<Album> albums) {
        return albums.stream()
                .map(AlbumsResponseTranslator::toEntertainment)
                .toList();
    }

    private static Entertainment toEntertainment(Album album) {
        return Entertainment.builder()
                .title(album.collectionName())
                .authors(List.of(album.artistName()))
                .entertainmentType(EntertainmentType.ALBUM)
                .build();
    }
}
