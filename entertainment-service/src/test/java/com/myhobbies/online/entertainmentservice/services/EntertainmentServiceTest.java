package com.myhobbies.online.entertainmentservice.services;

import com.myhobbies.online.entertainmentservice.clients.albumservice.AlbumsQueryService;
import com.myhobbies.online.entertainmentservice.clients.bookservice.BooksQueryService;
import com.myhobbies.online.entertainmentservice.exception.EntertainmentServiceException;
import com.myhobbies.online.entertainmentservice.models.Entertainment;
import com.myhobbies.online.entertainmentservice.models.EntertainmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class EntertainmentServiceTest {

    private EntertainmentService entertainmentService;
    private AlbumsQueryService albumsQueryService;
    private BooksQueryService booksQueryService;
    private Executor executor;

    @BeforeEach
    void setUp() {
        albumsQueryService = Mockito.mock(AlbumsQueryService.class);
        booksQueryService = Mockito.mock(BooksQueryService.class);
        executor = Mockito.mock(Executor.class);
        entertainmentService = new EntertainmentService(executor, albumsQueryService, booksQueryService);
    }

    @Test
    void getEntertainmentOptions_ValidRequest_ReturnsSortedEntertainmentOptions() throws Exception {
        List<Entertainment> albums = List.of(
                Entertainment.builder().title("Album B").entertainmentType(EntertainmentType.ALBUM).build(),
                Entertainment.builder().title("Album A").entertainmentType(EntertainmentType.ALBUM).build()
        );
        List<Entertainment> books = List.of(
                Entertainment.builder().title("Book B").entertainmentType(EntertainmentType.BOOK).build(),
                Entertainment.builder().title("Book A").entertainmentType(EntertainmentType.BOOK).build()
        );

        when(albumsQueryService.getAlbums(anyString())).thenReturn(albums);
        when(booksQueryService.getBooks(anyString())).thenReturn(books);

        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        List<Entertainment> result = entertainmentService.getEntertainmentOptions("Test");

        assertEquals(4, result.size());
        assertEquals("Album A", result.get(0).getTitle());
        assertEquals("Album B", result.get(1).getTitle());
        assertEquals("Book A", result.get(2).getTitle());
        assertEquals("Book B", result.get(3).getTitle());
    }

    @Test
    void getEntertainmentOptions_AlbumsServiceThrowsException_ThrowsEntertainmentServiceException() {
        when(albumsQueryService.getAlbums(anyString())).thenThrow(new RuntimeException("Service unavailable"));
        when(booksQueryService.getBooks(anyString())).thenReturn(List.of());

        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        assertThrows(EntertainmentServiceException.class, () -> entertainmentService.getEntertainmentOptions("Test"));
    }

    @Test
    void getEntertainmentOptions_BooksServiceThrowsException_ThrowsEntertainmentServiceException() {
        when(albumsQueryService.getAlbums(anyString())).thenReturn(List.of());
        when(booksQueryService.getBooks(anyString())).thenThrow(new RuntimeException("Service unavailable"));

        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        assertThrows(EntertainmentServiceException.class, () -> entertainmentService.getEntertainmentOptions("Test"));
    }
}