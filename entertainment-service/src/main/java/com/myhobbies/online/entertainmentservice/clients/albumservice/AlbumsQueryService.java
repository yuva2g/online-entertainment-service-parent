package com.myhobbies.online.entertainmentservice.clients.albumservice;

import com.myhobbies.online.entertainmentservice.clients.albumservice.response.Album;
import com.myhobbies.online.entertainmentservice.clients.albumservice.response.AlbumsResponseTranslator;
import com.myhobbies.online.entertainmentservice.config.ExternalService;
import com.myhobbies.online.entertainmentservice.exception.ExternalServiceException;
import com.myhobbies.online.entertainmentservice.models.Entertainment;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.SupplierUtils;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlbumsQueryService {

    private final RestTemplate albumsQueryRestTemplate;

    private final CircuitBreaker albumsQueryCircuitBreaker;

    @Timed(value = "http.album-query-service.request", extraTags = {"backend", "album-query-service", "operation", "getAlbums"})
    public List<Entertainment> getAlbums(String searchText) {
        Supplier<List<Entertainment>> responseSupplier = getAlbumResultsSupplier(searchText);

        return albumsQueryCircuitBreaker.executeSupplier(SupplierUtils.recover(responseSupplier, exception -> {
            log.warn("Error while retrieving albums from albums-query-service {}", exception.getMessage());
            if (log.isDebugEnabled()) {
                log.debug("Error details while retrieving albums from albums-query-service", exception);
            }
            return Collections.emptyList();
        }));
    }

    private Supplier<List<Entertainment>> getAlbumResultsSupplier(String searchText) {

        return CircuitBreaker.decorateSupplier(albumsQueryCircuitBreaker, () -> {
            try {
                ResponseEntity<List<Album>> responseEntity =
                        albumsQueryRestTemplate.exchange(
                                "/albums?searchText=" + searchText,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<>() {
                                }
                        );
                List<Album> albums = responseEntity.getBody();
                return AlbumsResponseTranslator.toEntertainments(albums);
            } catch (Exception e) {
                throw new ExternalServiceException(e.getMessage(), ExternalService.ALBUMS_QUERY_SERVICE, e);
            }
        });
    }
}
