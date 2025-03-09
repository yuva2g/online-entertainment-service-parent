package com.myhobbies.online.entertainmentservice.services;

import com.myhobbies.online.entertainmentservice.clients.albumservice.AlbumsQueryService;
import com.myhobbies.online.entertainmentservice.clients.bookservice.BooksQueryService;
import com.myhobbies.online.entertainmentservice.exception.EntertainmentServiceException;
import com.myhobbies.online.entertainmentservice.models.Entertainment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class EntertainmentService {

    @Qualifier("asyncExecutor")
    private final Executor executor;

    private final AlbumsQueryService albumsQueryService;
    private final BooksQueryService booksQueryService;

    public List<Entertainment> getEntertainmentOptions(String searchText) {

        List<Entertainment> entertainments = new ArrayList<>();

        asyncGetEntertainments(searchText, entertainments);

        entertainments.sort(Comparator.comparing(Entertainment::getTitle));
        return entertainments;
    }

    private void asyncGetEntertainments(String searchText, List<Entertainment> entertainments) {
        CompletableFuture<List<Entertainment>> albumsFuture =
                CompletableFuture.supplyAsync(() -> albumsQueryService.getAlbums(searchText), executor);
        CompletableFuture<List<Entertainment>> booksFuture =
                CompletableFuture.supplyAsync(() -> booksQueryService.getBooks(searchText), executor);
        try {
            List<Entertainment> albumEntertainments= albumsFuture.get();
            List<Entertainment> bookEntertainments= booksFuture.get();
            entertainments.addAll(albumEntertainments);
            entertainments.addAll(bookEntertainments);
        } catch (InterruptedException | ExecutionException e) {
            throw new EntertainmentServiceException("Error while fetching albums using async", e);
        }
    }
}
