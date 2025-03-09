package com.myhobbies.online.albumsqueryservice.adapter.endpoints;

import com.myhobbies.online.albumsqueryservice.adapter.endpoints.dto.AlbumDTO;
import com.myhobbies.online.albumsqueryservice.adapter.endpoints.mapper.AlbumsDTOMapper;
import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.config.ITunesProperties;
import com.myhobbies.online.albumsqueryservice.domain.model.Album;
import com.myhobbies.online.albumsqueryservice.domain.usecases.AlbumsQueryUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("albums")
@Validated
public class AlbumsQueryController {

    private final AlbumsQueryUseCase albumsQueryService;
    private final AlbumsDTOMapper albumsDTOMapper;
    private final ITunesProperties iTunesProperties;

    @GetMapping(produces = "application/json")
    public List<AlbumDTO> getAlbums(@Valid @NotNull @NotEmpty
                                    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Invalid characters in search text")
                                    @RequestParam String searchText,
                                    @Valid @Positive @RequestParam(value = "limit", required = false) Integer limit) {

        List<Album> albums = albumsQueryService.getAlbum(searchText, limit != null
                ? limit : iTunesProperties.getDefaultResultLimit());
        return albumsDTOMapper.mapToDTOs(albums);
    }
}
