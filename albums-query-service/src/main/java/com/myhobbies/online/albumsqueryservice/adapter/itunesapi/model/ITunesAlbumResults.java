package com.myhobbies.online.albumsqueryservice.adapter.itunesapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ITunesAlbumResults (Integer resultCount, List<ITunesAlbum> results){
}
