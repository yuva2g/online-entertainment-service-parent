package com.myhobbies.online.albumsqueryservice.adapter.itunesapi;

import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.config.exception.ITunesAPIException;
import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.mapper.ITunesAlbumMapper;
import com.myhobbies.online.albumsqueryservice.adapter.itunesapi.model.ITunesAlbumResults;
import com.myhobbies.online.albumsqueryservice.domain.model.Album;
import com.myhobbies.online.albumsqueryservice.domain.port.outbound.AlbumsQueryPort;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.SupplierUtils;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ITunesAlbumsAdapter implements AlbumsQueryPort {

    private final RestTemplate itunesRestTemplate;

    private final CircuitBreaker iTunesCircuitBreaker;

    @Override
    @Timed(value = "http.itunes-api.request", extraTags = {"backend", "itunes-api", "operation", "getAlbums"})
    public List<Album> getAlbums(String searchText, Integer limit) {
        Supplier<ITunesAlbumResults> responseSupplier = getITunesAlbumResultsSupplier(searchText, limit);

        ITunesAlbumResults responseObject = iTunesCircuitBreaker.executeSupplier(responseSupplier);

        return ITunesAlbumMapper.mapToAlbums(responseObject);
    }

    private Supplier<ITunesAlbumResults> getITunesAlbumResultsSupplier(String searchText, Integer limit) {

        Supplier<ITunesAlbumResults> iTunesAlbumResultsSupplier = () -> {
            try {
                return itunesRestTemplate.getForObject(
                        "/search?term={searchText}&limit={limit}&entity=album", ITunesAlbumResults.class, searchText, limit);
            } catch (Exception e) {
                throw new ITunesAPIException(e.getMessage(), e);
            }
        };
        return SupplierUtils.recover(iTunesAlbumResultsSupplier, exception -> {
            throw new ITunesAPIException("Error while calling iTunes API", exception);
        });
    }
}
